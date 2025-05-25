package com.core.halpme.api.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "chat_message_image")
public class ChatMessageImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private ChatMessage chatMessage;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "image_order", nullable = false)
    private int imageOrder;

    @Builder
    public ChatMessageImage(ChatMessage chatMessage, String imageUrl, int imageOrder) {
        this.chatMessage = chatMessage;
        this.imageUrl = imageUrl;
        this.imageOrder = imageOrder;
    }
}
