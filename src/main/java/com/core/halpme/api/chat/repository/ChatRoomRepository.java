package com.core.halpme.api.chat.repository;

import com.core.halpme.api.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {

    @Query("SELECT r FROM ChatRoom r JOIN r.chatRoomMembers m1 JOIN r.chatRoomMembers m2 " +
            "WHERE m1.email = :email1 AND m2.email = :email2")
    Optional<ChatRoom> findChatRoomByMemberEmails(@Param("email1") String email1,
                                                  @Param("email2") String email2);

    @Query("SELECT r FROM ChatRoom r JOIN r.chatRoomMembers m WHERE m.email = :email")
    List<ChatRoom> findAllByMemberEmail(@Param("email") String email);

    @Query("SELECT r FROM ChatRoom r " +
            "JOIN r.chatRoomMembers m1 " +
            "JOIN r.chatRoomMembers m2 " +
            "WHERE m1.email = :email1 AND m2.email = :email2 " +
            "AND SIZE(r.chatRoomMembers) = 2")
    Optional<ChatRoom> findRoomByTwoMembers(@Param("email1") String email1, @Param("email2") String email2);

}
