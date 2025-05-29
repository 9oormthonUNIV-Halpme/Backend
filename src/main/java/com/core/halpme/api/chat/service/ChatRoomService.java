package com.core.halpme.api.chat.service;


import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponseDto createChatRoomForPersonal(String roomMakerEmail, Long guestPostId);
    List<ChatRoomDto> getChatRoomsForUser(String email);
}
