package vn.dathocjava.dathocjava_sample.service.implement;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public TransactionResponse createTransaction(TransactionDTO transactionDTO) throws Exception {
        Category category = categoryRepo.findById(transactionDTO.getCategoryId()).orElseThrow(
                () -> new Exception("Not found category")
        );
        User user = userRepo.findByEmail(transactionDTO.getUserEmail()).orElseThrow(
                () -> new Exception("Not found User")
        );

        return TransactionMapper.toResponse
                (transactionRepo.save(TransactionMapper.toEntity(transactionDTO, user, category)));
    }

    @Override
    public Long deleteTransaction(Long transactionId) throws Exception {
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Not found transaction!"));
        transactionRepo.delete(transaction);
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
        TransactionResponse transactionResponse =  TransactionMapper.toResponse(transactionRepo.save(transactionUpdate));
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
}
