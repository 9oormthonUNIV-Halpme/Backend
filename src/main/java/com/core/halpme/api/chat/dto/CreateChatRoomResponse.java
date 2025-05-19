package com.core.halpme.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateChatRoomResponse {
    private String roomMakerEmail;
    private String guestEmail;
    private String chatRoomId;
}
