//  3. ConversationParticipant.java - Entity definition
package com.example.security.Other.ConversationParticipant;

import com.example.security.Other.Conversation.Conversation;
import com.example.security.Other.ConversationParticipantId.ConversationParticipantId;
import com.example.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_participants")
@IdClass(ConversationParticipantId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationParticipant {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "is_admin")
    @Builder.Default
    private Boolean isAdmin = false;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
        if (isAdmin == null) {
            isAdmin = false;
        }
    }
}

// ================================================================================================
