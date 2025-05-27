package com.core.halpme.api.chat.service;


import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponseDto createChatRoomForPersonal(String roomMakerEmail, String guestEmail);
    List<ChatRoomDto> getChatRoomsForUser(String email);
    String getOpponentNickname(String roomId, String currentUserEmail);
}
