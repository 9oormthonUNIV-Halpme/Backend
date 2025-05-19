package com.core.halpme.api.chat.dto;

import lombok.Getter;

/**
 * 채팅방 개설 요청 dto
 */
@Getter
public class CreateChatRoomRequest {
    private String roomMakerEmail;
    private String guestEmail;
}
