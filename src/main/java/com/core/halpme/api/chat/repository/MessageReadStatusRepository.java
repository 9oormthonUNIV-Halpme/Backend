package com.core.halpme.api.chat.repository;

import com.core.halpme.api.chat.entity.MessageReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus,Long> {
    Optional<MessageReadStatus> findByMessageIdAndReaderEmail(Long messageId, String readerEmail);
    long countByMessageRoomIdAndReaderEmailAndIsReadFalse(String roomId, String readerEmail);
    List<MessageReadStatus> findAllByMessageId(Long messageId);

    @Query("""
    SELECT m FROM MessageReadStatus m
    WHERE m.readerEmail = :readerEmail
      AND m.isRead = false
      AND m.message.id <= :messageId
      AND m.message.roomId = :roomId
""")
    List<MessageReadStatus> findAllUnreadByReaderEmailAndRoomIdBeforeMessageId(
            @Param("readerEmail") String readerEmail,
            @Param("roomId") String roomId,
            @Param("messageId") Long messageId
    );

    // MessageReadStatusRepository.java
    @Query("SELECT m FROM MessageReadStatus m " +
            "WHERE m.readerEmail = :readerEmail " +
            "AND m.message.roomId = :roomId " +
            "AND m.isRead = false")
    List<MessageReadStatus> findAllUnreadByReaderEmailAndRoomId(@Param("readerEmail") String readerEmail,
                                                                @Param("roomId") String roomId);

}
