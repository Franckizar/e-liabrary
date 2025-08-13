// 1. Message.java - First we need to define the Message entity with proper enums
package com.example.security.Other.Message;

import com.example.security.Other.Conversation.Conversation;
import com.example.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    public enum MessageType {
        TEXT,
        IMAGE,
        FILE,
        AUDIO,
        SYSTEM,
        EDITED,
        DELETED
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = false;
        }
        if (messageType == null) {
            messageType = MessageType.TEXT;
        }
    }
}