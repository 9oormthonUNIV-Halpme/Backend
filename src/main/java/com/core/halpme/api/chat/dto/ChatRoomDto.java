package com.core.halpme.api.chat.dto;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
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
    private long unreadCount;

    public static ChatRoomDto fromEntity(ChatRoom room, String currentUserEmail, MessageReadStatusRepository readRepo) {
        List<String> memberEmails = room.getChatRoomMembers().stream()
                .map(Member::getEmail)
                .toList();

        ChatMessage last = room.getLastChatMesg();

        long unread = readRepo.countByMessageRoomIdAndReaderEmailAndIsReadFalse(room.getId(), currentUserEmail);

        return ChatRoomDto.builder()
                .roomId(room.getId())
                .participants(memberEmails)
                .lastMessage(last != null ? ChatMessageDto.fromEntity(last) : null)
                .unreadCount(unread)
                .build();
    }

}
