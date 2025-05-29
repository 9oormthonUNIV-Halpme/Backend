package com.core.halpme.api.chat.controller;

import com.core.halpme.api.chat.dto.*;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.service.ChatMessageService;
import com.core.halpme.api.chat.service.ChatRoomService;
import com.core.halpme.common.response.ApiResponse;
import com.core.halpme.common.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Chatting", description = "Chatting 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/chatRoom/")
@Slf4j
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    @PostMapping("/personal")
    @Operation(summary = "두 유저 사이의 채팅방 생성", description = "해당 게시글의 PostId 필요합니다.")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CreateChatRoomResponseDto>> createPersonalChatRoom(@RequestBody GuestPostIdDto request) {

        String roomMakerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        CreateChatRoomResponseDto response = chatRoomService.createChatRoomForPersonal(roomMakerEmail, request.getGuestPostId());

        return ApiResponse.success(SuccessStatus.CHAT_ROOM_CREATE_SUCCESS, response);
    }



    @GetMapping("/messages")
    @Operation(summary = "채팅방 채팅 기록 반환", description = "RoomId 필요")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessagesByRoomId(@RequestParam String roomId) {

        List<ChatMessage> messages = chatMessageService.getMessagesByRoomId(roomId);
        List<ChatMessageDto> response = messages.stream()
                .map(ChatMessageDto::fromEntity)
                .collect(Collectors.toList());

        return ApiResponse.success(SuccessStatus.CHAT_MESSAGES_GET_SUCCESS, response);
    }


    @GetMapping("/rooms")
    @Operation(summary = "사용자의 채팅방 목록 보기", description = "인증 토큰 필요")
    public ResponseEntity<ApiResponse<List<ChatRoomDto>>> getChatRooms() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChatRoomDto> rooms = chatRoomService.getChatRoomsForUser(userEmail);

        return ApiResponse.success(SuccessStatus.CHATROOM_LIST_SUCCESS, rooms);
    }

}