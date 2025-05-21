package com.core.halpme.api.chat.controller;

import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomRequestDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.entity.MessageReadStatus;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.chat.service.ChatMessageService;
import com.core.halpme.api.chat.service.ChatRoomService;
import com.core.halpme.api.members.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chatRoom/")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final SecurityUtil securityUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageReadStatusRepository messageReadStatusRepository;

    @PostMapping("/personal") // 두 유저 사이의 채팅방 생성(두 유저의 이메일 필요) --> 이메일로 넣을 지 고민 필요
    public CreateChatRoomResponseDto createPersonalChatRoom(@RequestBody CreateChatRoomRequestDto request) {
        return chatRoomService.createChatRoomForPersonal(request);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessagesByRoomId(
            @RequestParam String roomId
    ) {
        List<ChatMessage> messages = chatMessageService.getMessagesByRoomId(roomId);
        List<ChatMessageDto> response = messages.stream()
                .map(ChatMessageDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/rooms") // 모든 체팅방 목록 보기
    public ResponseEntity<List<ChatRoomDto>> getChatRooms(@RequestParam String userEmail) {
        List<ChatRoomDto> rooms = chatRoomService.getChatRoomsForUser(userEmail);
        return ResponseEntity.ok(rooms);
    }

}