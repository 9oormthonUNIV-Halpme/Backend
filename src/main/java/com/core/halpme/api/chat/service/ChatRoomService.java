package com.core.halpme.api.chat.service;


import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomRequestDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponseDto createChatRoomForPersonal(CreateChatRoomRequestDto request);
    List<ChatRoomDto> getChatRoomsForUser(String email);
}
