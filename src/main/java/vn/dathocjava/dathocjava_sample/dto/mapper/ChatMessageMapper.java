package vn.dathocjava.dathocjava_sample.dto.mapper;

import vn.dathocjava.dathocjava_sample.dto.request.ChatMessageDTO;
import vn.dathocjava.dathocjava_sample.model.MessageChat;

public class ChatMessageMapper {

    public static MessageChat toEntity(ChatMessageDTO dto) {
        MessageChat entity = new MessageChat();
        entity.setEmail(dto.getEmail());
        entity.setFromUser(dto.getFromUser());
        entity.setMessage(dto.getMessage());
        entity.setNewChat(dto.getIsNewChat());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }

}
