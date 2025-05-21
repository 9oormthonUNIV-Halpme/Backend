package com.core.halpme.api.chat.controller;

import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomRequest;
import com.core.halpme.api.chat.dto.CreateChatRoomResponse;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.service.ChatMessageService;
import com.core.halpme.api.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chatRoom/")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/personal")
    public CreateChatRoomResponse createPersonalChatRoom(@RequestBody CreateChatRoomRequest request) {
        return chatRoomService.createChatRoomForPersonal(request);
    }

    @GetMapping("/messages") //두 유저가 주고 받은 메시지 목록
    public ResponseEntity<List<ChatMessageDto>> getMessagesBetweenUsers(
            @RequestParam String email1,
            @RequestParam String email2
    ) {
        List<ChatMessage> messages = chatMessageService.getMessagesBetween(email1, email2);
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