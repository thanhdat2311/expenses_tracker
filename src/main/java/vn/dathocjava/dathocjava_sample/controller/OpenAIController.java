package vn.dathocjava.dathocjava_sample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dathocjava.dathocjava_sample.service.implement.OpenAIService;

@RestController
@RequestMapping("expenses-tracker/v1/chatAI")
@RequiredArgsConstructor
public class OpenAIController {
    private final OpenAIService openAIService;
    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String chatRequest) {
        String response = openAIService.chat(chatRequest);
        return ResponseEntity.ok(response);
    }
}

