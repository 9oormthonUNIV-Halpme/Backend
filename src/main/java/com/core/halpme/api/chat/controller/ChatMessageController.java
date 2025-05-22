package com.core.halpme.api.chat.controller;


import com.core.halpme.api.chat.WebSocketEventListener;
import com.core.halpme.api.chat.auth.StompUtil;
import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.MessageReadStatus;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.chat.service.ChatMessageService;
import com.core.halpme.api.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;
import java.util.Set;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageReadStatusRepository messageReadStatusRepository;

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessageDto message, Message<?> rawMessage, Principal principal) {
        if (principal == null) {
            // fallback: 세션에서 user 꺼내기
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(rawMessage);
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                Object sessionUser = sessionAttributes.get("user");
                if (sessionUser instanceof Principal p) {
                    principal = p;
                }
            }
        }

        if (principal == null) {
            log.warn("메시지 발신자 정보 없음. Principal이 null임");
            return;
        }

        String senderEmail = principal.getName();
        message.setSender(senderEmail);

        ChatMessage saved = chatMessageService.createChatMessage(message);

        messagingTemplate.convertAndSend("/sub/channel/" + message.getRoomId(), ChatMessageDto.fromEntity(saved));
    }




    @MessageMapping("/read")
    public void markAsRead(@Payload Long messageId, Message<?> rawMessage) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(rawMessage);
        Principal principal = accessor.getUser();

        if (principal == null) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                Object sessionUser = sessionAttributes.get("user");
                if (sessionUser instanceof Principal p) {
                    principal = p;
                }
            }
        }
        System.out.println(principal);
        if (principal == null) {
            log.warn("읽음 처리 실패: principal이 null입니다.");
            return;
        }
        String readerEmail = principal.getName();

        MessageReadStatus status = messageReadStatusRepository
                .findByMessageIdAndReaderEmail(messageId, readerEmail)
                .orElseThrow(() -> new RuntimeException("읽음 상태를 찾을 수 없습니다."));

        if (!status.isRead()) {
            status.setRead(true);
            messageReadStatusRepository.save(status);
        }
    }




}