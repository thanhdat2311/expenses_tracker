package vn.dathocjava.dathocjava_sample.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import vn.dathocjava.dathocjava_sample.components.JwtTokenUtil;
import vn.dathocjava.dathocjava_sample.dto.mapper.TransactionMapper;
import vn.dathocjava.dathocjava_sample.dto.request.TransactionDTO;
import vn.dathocjava.dathocjava_sample.dto.response.PageResponse;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Category;
import vn.dathocjava.dathocjava_sample.model.Transaction;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.repository.CategoryRepo;
import vn.dathocjava.dathocjava_sample.repository.TransactionRepo;
import vn.dathocjava.dathocjava_sample.repository.UserRepo;
import vn.dathocjava.dathocjava_sample.service.interfaceClass.ITransactionService;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final JwtTokenUtil jwtTokenUtil;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public TransactionResponse createTransaction(TransactionDTO transactionDTO) throws Exception {
        Category category = categoryRepo.findById(transactionDTO.getCategoryId()).orElseThrow(
                () -> new Exception("Not found category")
        );
        User user = userRepo.findByEmail(transactionDTO.getUserEmail()).orElseThrow(
                () -> new Exception("Not found User")
        );
        TransactionResponse transactionResponse = TransactionMapper.toResponse
                (transactionRepo.save(TransactionMapper.toEntity(transactionDTO, user, category)));
        String message = objectMapper.writeValueAsString(Map.of(
                "group", "monthly-report-group",
                "userEmail",transactionDTO.getUserEmail(),
                "todo","[TRANSACTION-EVENT] - CREATE",
                "targetId",transactionResponse.getId(),
                "timeStamp", Instant.now()
        ));
        kafkaTemplate.send("transaction-event", message);
        return transactionResponse;
    }

    @Override
    public Long deleteTransaction(Long transactionId) throws Exception {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Not found transaction!"));
        transactionRepo.delete(transaction);
        String message = objectMapper.writeValueAsString(Map.of(
                "group", "monthly-report-group",
                "userEmail",transaction.getUser().getEmail(),
                "todo","[TRANSACTION-EVENT] - DELETE",
                "targetId",transaction.getId(),
                "timeStamp", Instant.now()
        ));
        kafkaTemplate.send("transaction-event", message);
        return transactionId;
    }
    @Override
    public TransactionResponse updateTransaction(Long transactionId, TransactionDTO transactionDTO) throws Exception {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Not found transaction!"));
        Category category = categoryRepo.findById(transactionDTO.getCategoryId()).orElseThrow(
                () -> new Exception("Not found category")
        );
        User user = userRepo.findByEmail(transactionDTO.getUserEmail()).orElseThrow(
                () -> new Exception("Not found User")
        );
        Transaction transactionUpdate = TransactionMapper.toEntity(transactionDTO,user,category);
        transactionUpdate.setId(transactionId);
        transactionUpdate.setCreatedAt(transaction.getCreatedAt());
        TransactionResponse transactionResponse =  TransactionMapper.toResponse(transactionRepo.save(transactionUpdate));
        String message = objectMapper.writeValueAsString(Map.of(
                "group", "monthly-report-group",
                "userEmail",transactionDTO.getUserEmail(),
                "todo","[TRANSACTION-EVENT] - UPDATE - ID: " ,
                "targetId",transaction.getId(),
                "timeStamp", Instant.now()
        ));
        kafkaTemplate.send("transaction-event", message);
        return transactionResponse;
    }


    @Override
    public PageResponse<List<TransactionResponse>> getAllTransaction(String token,
                                                                     int pageNo,
                                                                     int pageSize,
                                          String sortBy){
        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)){
            //id:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher =pattern.matcher(sortBy);
            if (matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                }
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageableTransaction = PageRequest.of(pageNo,
                pageSize,
                Sort.by(sorts));
        Long userId = jwtTokenUtil.extractUserId(token);
        Page<Transaction> transactionPage = transactionRepo.findAllByUserId( userId,pageableTransaction);
List<TransactionResponse> transactionResponseList = transactionPage.getContent()
        .stream()
        .map(TransactionMapper::toResponse).toList();
        return PageResponse.<List<TransactionResponse>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(transactionPage.getTotalPages())
                .items(transactionResponseList)
                .build();
    }

    @Override
    @KafkaListener(topics = "transaction-event", groupId = "monthly-report-group")
    public void monthlyReportGroup(String message)  throws JsonProcessingException {
        Map<String, Object> data = objectMapper.readValue(message, new TypeReference<>() {});
        String group = String.valueOf(data.get("group"));
        if (group.equals("monthly-report-group")) {
            String email = String.valueOf(data.get("userEmail"));
            System.out.println("KAFKA-TOPIC-monthly-GROUP:" + group);
            List<Transaction> transactionList = transactionRepo.findAllByUserEmail(email);
            Map<Integer, Double> totalByMonth = transactionList.stream()
                    .collect(Collectors.groupingBy(
                            tran -> {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(tran.getTransactionDate());
                                return cal.get(Calendar.MONTH) + 1;  // Tháng trả về từ 0 - 11 nên +1 cho đúng tháng 1-12
                            } ,     // Lấy tháng (1-12)
                            Collectors.summingDouble(tran -> tran.getAmount().doubleValue())  // Tính tổng amount cho từng tháng
                    ));

        }
        System.out.println("TOTAL-BY-MONTH: "+
                String.valueOf(data.get("todo")) + " - " +
                String.valueOf(data.get("targetId")) + " - " +
                        String.valueOf(data.get("userEmail"))+ " - " +
                        String.valueOf(data.get("timeStamp"))
                );
    }
}

