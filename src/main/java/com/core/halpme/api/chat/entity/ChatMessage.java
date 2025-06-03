package com.core.halpme.api.chat.entity;

import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chat_message")
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseTimeEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "roomId", insertable = false, updatable = false)
    private String roomId;

    @JoinColumn(name = "sender", insertable = false, updatable = false)
    private String sender; //이메일

    @Column(name = "message")
    private String message;

    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ChatMessageImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MessageType messageType;
}
