package com.core.halpme.api.chat.repository;

import com.core.halpme.api.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId(String roomId); // 대화방별 메시지 조회용
    List<ChatMessage> findByRoomId(String roomId);
}