package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.entity.MessageReadStatus;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.members.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MessageReadStatusRepository messageReadStatusRepository;

    @Override
    public ChatMessage createChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = chatMessageDto.toEntity();

        // 1. 메시지 저장
        ChatMessage saved = chatMessageRepository.save(chatMessage);

        // 2. 채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.findById(saved.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));

        // 3. 채팅방에 마지막 메시지 지정
        chatRoom.setLastChatMesg(saved);
        chatRoomRepository.save(chatRoom);

        // 4. 읽음 상태 저장 (모든 참여자)
        for (Member participant : chatRoom.getChatRoomMembers()) {
            boolean isSender = participant.getEmail().equals(chatMessage.getSender());

            messageReadStatusRepository.save(
                    MessageReadStatus.builder()
                            .message(saved)
                            .readerEmail(participant.getEmail())
                            .isRead(isSender) // 보낸 사람은 읽음 true, 나머진 false
                            .build()
            );
        }

        return saved;
    }



    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }
}