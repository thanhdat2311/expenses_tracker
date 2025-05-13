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
import vn.dathocjava.dathocjava_sample.dto.mapper.TransactionMapper;
import vn.dathocjava.dathocjava_sample.dto.response.TransactionResponse;
import vn.dathocjava.dathocjava_sample.model.Transaction;
import vn.dathocjava.dathocjava_sample.repository.TransactionRepo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {
    private final TransactionRepo transactionRepo;
    @Value("${OPENAI_API_KEY}")
    private String apiKey;

    public String chat(String message) {
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
                Map.of("role", "user", "content", message)
        );
        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://api.openai.com/v1/chat/completions", request, String.class);
        return response.getBody();
    }
}

