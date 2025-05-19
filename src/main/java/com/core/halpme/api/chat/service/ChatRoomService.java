package com.core.halpme.api.chat.service;


import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomRequest;
import com.core.halpme.api.chat.dto.CreateChatRoomResponse;

import java.util.List;

public interface ChatRoomService {
    CreateChatRoomResponse createChatRoomForPersonal(CreateChatRoomRequest request);
    List<ChatRoomDto> getChatRoomsForUser(String email);
}
