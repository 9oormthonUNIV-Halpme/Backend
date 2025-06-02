package com.core.halpme.api.chat.controller;


import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.dto.ReadStatusMessage;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.MessageReadStatus;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageReadStatusRepository messageReadStatusRepository;
    private final ChatMessageRepository chatMessageRepository;

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

        messagingTemplate.convertAndSend(
                "/sub/channel/" + message.getRoomId(),
                ChatMessageDto.fromEntity(saved, true)
        );
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

        if (principal == null) {
            log.warn("읽음 처리 실패: principal이 null입니다.");
            return;
        }

        String readerEmail = principal.getName();

        // messageId로 메시지 조회해서 roomId 추출
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        String roomId = message.getRoomId();

        // roomId 포함해서 안 읽은 메시지 가져오기
        List<MessageReadStatus> unreadStatuses =
                messageReadStatusRepository.findAllUnreadByReaderEmailAndRoomIdBeforeMessageId(readerEmail, roomId, messageId);

        if (unreadStatuses.isEmpty()) {
            log.info("읽을 메시지가 없습니다.");
            return;
        }

        for (MessageReadStatus status : unreadStatuses) {
            status.setRead(true);
        }
        messageReadStatusRepository.saveAll(unreadStatuses);

        messagingTemplate.convertAndSend(
                "/sub/channel/" + roomId + "/read-status",
                new ReadStatusMessage(readerEmail, unreadStatuses.stream()
                        .map(status -> status.getMessage().getId())
                        .toList())
        );

        log.info("총 {}개의 메시지를 읽음 처리했습니다.", unreadStatuses.size());
    }

    @MessageMapping("/read-room")
    public void markRoomAsRead(@Payload String roomId, Message<?> rawMessage) {
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

        if (principal == null) {
            log.warn("읽음 처리 실패 (/read-room): principal이 null입니다.");
            return;
        }

        String readerEmail = principal.getName();

        // 안 읽은 메시지 중 가장 마지막 메시지 ID 찾기
        List<MessageReadStatus> unreadList =
                messageReadStatusRepository.findAllUnreadByReaderEmailAndRoomId(readerEmail, roomId);

        if (unreadList.isEmpty()) {
            log.info("읽을 메시지가 없습니다. (/read-room)");
            return;
        }

        for (MessageReadStatus status : unreadList) {
            status.setRead(true);
        }

        messageReadStatusRepository.saveAll(unreadList);

        messagingTemplate.convertAndSend(
                "/sub/channel/" + roomId + "/read-status",
                new ReadStatusMessage(readerEmail, unreadList.stream()
                        .map(status -> status.getMessage().getId())
                        .toList())
        );

        log.info("총 {}개의 메시지를 읽음 처리했습니다. (/read-room)", unreadList.size());
    }




}