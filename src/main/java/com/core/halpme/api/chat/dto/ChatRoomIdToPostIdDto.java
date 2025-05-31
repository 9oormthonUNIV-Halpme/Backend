package com.core.halpme.api.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomIdToPostIdDto {

    private Long postId;

    public ChatRoomIdToPostIdDto(Long postId) {
        this.postId = postId;
    }
}
