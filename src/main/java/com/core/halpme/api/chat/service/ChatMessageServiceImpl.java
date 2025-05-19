package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatMessageDto;
import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatMessage createChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = chatMessageDto.toEntity();

        //DB에 메시지 저장
        ChatMessage saved = chatMessageRepository.save(chatMessage);

        //채팅방에 최근 메시지로 설정
        ChatRoom chatRoom = chatRoomRepository.findById(saved.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방 없음"));
        chatRoom.setLastChatMesg(saved);
        chatRoomRepository.save(chatRoom);

        return saved;
    }

    public List<ChatMessage> getMessagesBetween(String email1, String email2) {
        // 두 이메일을 모두 포함한 ChatRoom 찾기
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByMemberEmails(email1, email2)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        // 해당 방 ID로 메시지 조회
        return chatMessageRepository.findByRoomId(chatRoom.getId());
    }

}