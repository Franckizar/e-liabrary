
// 2. Conversation.java - Entity definition
package com.example.security.Other.Conversation;

import com.example.security.Other.ConversationParticipant.ConversationParticipant;
import com.example.security.Other.Message.Message;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_group_chat")
    @Builder.Default
    private Boolean isGroupChat = false;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ConversationParticipant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("timestamp ASC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isGroupChat == null) {
            isGroupChat = false;
        }
    }

    public void addParticipant(ConversationParticipant participant) {
        participants.add(participant);
        participant.setConversation(this);
    }

    public void removeParticipant(ConversationParticipant participant) {
        participants.remove(participant);
        participant.setConversation(null);
    }
}
