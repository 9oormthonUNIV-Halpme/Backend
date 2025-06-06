package com.core.halpme.api.chat.controller;

import com.core.halpme.api.chat.dto.*;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.MessageReadStatus;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
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
    private final MessageReadStatusRepository messageReadStatusRepository;

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessagesByRoomId(@RequestParam String roomId) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChatMessage> messages = chatMessageService.getMessagesByRoomId(roomId);

        //채팅방 멤버 중 상대방 이메일 찾기
        String opponentEmail = chatRoomService.getChatOpponentInfo(roomId, myEmail).getOpponentEmail();

        List<ChatMessageDto> response = messages.stream()
                .map(msg -> {
                    // 내가 보낸 메시지면 -> 상대방이 읽었는지 확인
                    boolean isReadByOpponent = msg.getSender().equals(myEmail)
                            ? messageReadStatusRepository
                            .findByMessageIdAndReaderEmail(msg.getId(), opponentEmail)
                            .map(MessageReadStatus::isRead)
                            .orElse(false)
                            : true; // 상대방이 보낸 메시지면 항상 읽은 것으로 간주

                    return ChatMessageDto.fromEntity(msg, isReadByOpponent);
                })
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

    @GetMapping("/opponent-info")
    @Operation(summary = "채팅방 ID로 상대방 닉네임, 사용자 신분(봉사요청, 도움요청) 반환", description = "채팅방 ID 필요, JWT 인증 필요")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<OpponentInfoDto>> getOpponentInfo(@RequestParam String roomId) {
        String myEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        OpponentInfoDto dto = chatRoomService.getChatOpponentInfo(roomId, myEmail);
        return ApiResponse.success(SuccessStatus.CHAT_OPPONENT_NICKNAME_SUCCESS, dto);
    }

    @Operation(
            summary = "채팅방ID로 상대방의 봉사 요청글 PostId 반환",
            description = "채팅방ID 필요"
    )
    @GetMapping("/{chatRoomId}/post")
    public ResponseEntity<ApiResponse<ChatRoomIdToPostIdDto>> getPostIdByChatRoomId(@PathVariable String chatRoomId) {

        ChatRoomIdToPostIdDto dto = chatRoomService.getPostIdByChatRoomId(chatRoomId);

        return ApiResponse.success(SuccessStatus.POST_ID_GET_SUCCESS, dto);
    }
}