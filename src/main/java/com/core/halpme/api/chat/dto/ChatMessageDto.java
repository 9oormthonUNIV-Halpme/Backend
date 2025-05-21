package com.core.halpme.api.chat.dto;

import com.core.halpme.api.chat.entity.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 웹소켓 접속시 요청 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private String message;

    /* Dto -> Entity */
    public ChatMessage toEntity() {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .build();
        return chatMessage;
    }

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return new ChatMessageDto(
                chatMessage.getRoomId(),
                chatMessage.getSender(),
                chatMessage.getMessage()
        );
    }
}