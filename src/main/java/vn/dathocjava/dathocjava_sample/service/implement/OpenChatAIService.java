package vn.dathocjava.dathocjava_sample.service.implement;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.dathocjava.dathocjava_sample.dto.mapper.ChatMessageMapper;
import vn.dathocjava.dathocjava_sample.dto.mapper.TransactionMapper;
import vn.dathocjava.dathocjava_sample.dto.receive.MessageReceive;
import vn.dathocjava.dathocjava_sample.dto.request.ChatMessageDTO;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.MessageChat;
import vn.dathocjava.dathocjava_sample.model.Transaction;
import vn.dathocjava.dathocjava_sample.repository.ChatMessageRepo;
import vn.dathocjava.dathocjava_sample.repository.TransactionRepo;
import vn.dathocjava.dathocjava_sample.service.interfaceClass.IOpenChatAIService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenChatAIService implements IOpenChatAIService {
    private final TransactionRepo transactionRepo;
    private final ChatMessageRepo chatMessageRepo;
    //@Value("${OPENAI_API_KEY}")
    private String apiKey = System.getenv("OPENAI_API_KEY");
    public String chat(ChatMessageDTO chatMessageDTO) {
        chatMessageRepo.save(ChatMessageMapper.toEntity(chatMessageDTO));
        List<Transaction> transactionList = transactionRepo.findAll();
        RestTemplate restTemplate = new RestTemplate();
        List<TransactionResponse> transactionResponseList = transactionList
                .stream()
                .map(TransactionMapper::toResponse).toList();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");

        List<Map<String, String>> messages = List.of(
                Map.of("role", "system", "content",
                        "Bạn là 1 chuyên gia tài chính với dữ liệu sau hãy trả lời câu hỏi: "
                                + new Gson().toJson(transactionResponseList)),
                Map.of("role", "user", "content", chatMessageDTO.getMessage())
        );
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<MessageReceive> response = restTemplate
                .postForEntity("https://api.openai.com/v1/chat/completions",
                        request,
                        MessageReceive.class);
        String messageResponse = response.getBody().getChoices().get(0).getMessage().getContent();
        MessageChat messageChatResponse = new MessageChat(null,
                chatMessageDTO.getEmail(),
                false,
                messageResponse,
                true,false, new Date()
        );
        chatMessageRepo.save(messageChatResponse);
        return response.getBody().getChoices().get(0).getMessage().getContent();
    }

    @Override
    public List<MessageChat> getChatByEmail(String email) {
        return chatMessageRepo.findAllByEmail(email);
    }

    @Override
    public String deleteChatByEmail(String email) {
        List<MessageChat> messageChats = this.getChatByEmail(email);
        chatMessageRepo.softDeleteByEmail(email);
        return "Delete Successfully!";
    }
}

