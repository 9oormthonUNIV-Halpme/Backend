package com.core.halpme.api.chat.controller;


import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.service.ChatMessageService;
import com.core.halpme.api.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("api/v1/message")
    public void sendMessage(ChatMessageDto message) {
        // 실시간으로 방에서 채팅하기
        ChatMessage newChat = chatMessageService.createChatMessage(message);
        log.info("received message: {}", message);

        // 방에 있는 모든 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/sub/channel/"+message.getRoomId(), newChat);
    }


}