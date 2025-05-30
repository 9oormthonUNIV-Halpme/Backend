package com.core.halpme.api.chat.service;


import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.ChatRoomIdToPostIdDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;
import com.core.halpme.api.chat.dto.OpponentInfoDto;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponseDto createChatRoomForPersonal(String roomMakerEmail, Long guestPostId);
    List<ChatRoomDto> getChatRoomsForUser(String email);
    OpponentInfoDto getChatOpponentInfo(String roomId, String currentUserEmail);
    ChatRoomIdToPostIdDto getPostIdByChatRoomId(String roomId);
}
