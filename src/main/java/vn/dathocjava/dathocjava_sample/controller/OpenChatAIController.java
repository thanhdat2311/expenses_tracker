package vn.dathocjava.dathocjava_sample.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.dathocjava.dathocjava_sample.components.JwtTokenUtil;
import vn.dathocjava.dathocjava_sample.dto.request.ChatMessageDTO;
import vn.dathocjava.dathocjava_sample.model.MessageChat;
import vn.dathocjava.dathocjava_sample.response.ResponseData;
import vn.dathocjava.dathocjava_sample.service.implement.OpenChatAIService;

import java.util.List;

@RestController
@RequestMapping("expenses-tracker/v1/chatAI")
@RequiredArgsConstructor
public class OpenChatAIController {
    private final OpenChatAIService openChatAIService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public ResponseEntity<?> chat(@Valid @RequestBody ChatMessageDTO chatMessageDTO,
                                       @RequestHeader("Authorization") String token,
                                  BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": "
                            + fieldError.getDefaultMessage()).toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseData<>(HttpStatus.BAD_REQUEST.value(), errorMessages.toString()));
        }
        token = token.substring(7);
        String email = jwtTokenUtil.extractEmail(token);
        chatMessageDTO.setEmail(email);
        String response = openChatAIService.chat(chatMessageDTO);
        return ResponseEntity
                .ok(new ResponseData<>(HttpStatus.ACCEPTED.value(), "Create successfully!"
                        , response));
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getChatByEmail(
                                  @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String email = jwtTokenUtil.extractEmail(token);
        List<MessageChat> messageChats = openChatAIService.getChatByEmail(email);
        return ResponseEntity
                .ok(new ResponseData<>(HttpStatus.ACCEPTED.value()
                        , "Create successfully!"
                        , messageChats ));
    }
    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteChatByEmail(
            @RequestHeader("Authorization") String token) {

        token = token.substring(7);
        String email = jwtTokenUtil.extractEmail(token);
        String messageDelete = openChatAIService.deleteChatByEmail(email);
        return ResponseEntity
                .ok(new ResponseData<>(HttpStatus.ACCEPTED.value()
                        , "Create successfully!"
                        , messageDelete ));
    }
}

