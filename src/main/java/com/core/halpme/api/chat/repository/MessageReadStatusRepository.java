package com.core.halpme.api.chat.repository;

import com.core.halpme.api.chat.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus,Long> {
    Optional<MessageReadStatus> findByMessageIdAndReaderEmail(Long messageId, String readerEmail);
    long countByMessageRoomIdAndReaderEmailAndIsReadFalse(String roomId, String readerEmail);
    List<MessageReadStatus> findAllByMessageId(Long messageId);

}
