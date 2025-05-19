package com.core.halpme.api.chat.dto;

import com.core.halpme.api.chat.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private List<String> participants;
    private ChatMessageDto lastMessage;

    public static ChatRoomDto fromEntity(com.core.halpme.api.chat.entity.ChatRoom room) {
        List<String> memberEmails = room.getChatRoomMembers().stream()
                .map(member -> member.getEmail())
                .toList();

        ChatMessage last = room.getLastChatMesg();

        return ChatRoomDto.builder()
                .roomId(room.getId())
                .participants(memberEmails)
                .lastMessage(last != null ? ChatMessageDto.fromEntity(last) : null)
                .build();
    }
}
