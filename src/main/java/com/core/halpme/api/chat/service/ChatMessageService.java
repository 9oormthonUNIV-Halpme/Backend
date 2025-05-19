package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.entity.ChatMessage;

import java.util.List;
import java.util.UUID;

public interface ChatMessageService {
    ChatMessage createChatMessage(ChatMessageDto dto);
    List<ChatMessage> getMessagesBetween(String email1, String email2);
}
