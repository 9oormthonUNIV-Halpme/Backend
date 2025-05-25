package com.core.halpme.api.chat.dto;

import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatMessageImage;
import com.core.halpme.api.chat.entity.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 웹소켓 접속시 요청 Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private Long id;
    private String roomId;
    private String sender;
    private String message;
    private List<String> imageUrls;
    private MessageType messageType;
    private LocalDateTime createdAt;


    /* Dto -> Entity */
    public ChatMessage toEntity() {

        return ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .messageType(messageType)
                .build();
    }

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {

        List<String> imageUrls = chatMessage.getImages().stream()
                .sorted((a, b) -> Integer.compare(a.getImageOrder(), b.getImageOrder()))
                .map(ChatMessageImage::getImageUrl)
                .toList();

        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .imageUrls(imageUrls)
                .messageType(chatMessage.getMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}