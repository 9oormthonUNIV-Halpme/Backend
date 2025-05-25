package com.core.halpme.api.chat.repository;

import com.core.halpme.api.chat.entity.ChatMessage;
import com.core.halpme.api.chat.entity.ChatMessageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageImageRepository extends JpaRepository<ChatMessageImage, Long> {

    List<ChatMessageImage> findAllByChatMessageOrderByImageOrderAsc(ChatMessage chatMessage);
}
