package vn.dathocjava.dathocjava_sample.service.interfaceClass;

import vn.dathocjava.dathocjava_sample.dto.request.ChatMessageDTO;
import vn.dathocjava.dathocjava_sample.model.MessageChat;

import java.util.List;

public interface IOpenChatAIService {
     String chat(ChatMessageDTO chatMessageDTO);
     List<MessageChat> getChatByEmail( String email);
     String deleteChatByEmail(String email);
}
