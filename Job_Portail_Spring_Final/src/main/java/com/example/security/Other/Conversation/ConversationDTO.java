
// ConversationDTO.java
package com.example.security.Other.Conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.example.security.Other.Message.ChatMessageDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String title;
    private boolean isGroupChat;
    private int participantCount;
    private List<ParticipantDTO> participants;
    private ChatMessageDTO lastMessage;
    private long unreadCount;
    private LocalDateTime lastActivity;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParticipantDTO {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private String fullName;
        private LocalDateTime joinedAt;
        private boolean isOnline;

        public ParticipantDTO(Long id, String firstname, String lastname, String email, LocalDateTime joinedAt) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.joinedAt = joinedAt;
            this.fullName = (firstname != null ? firstname : "") + " " + (lastname != null ? lastname : "");
        }
    }

    // Generate conversation title based on participants
    public String generateTitle(Long currentUserId) {
        if (this.title != null && !this.title.isEmpty()) {
            return this.title;
        }

        if (participants == null || participants.isEmpty()) {
            return "Empty Conversation";
        }

        if (participants.size() == 2) {
            // Direct message - show other participant's name
            return participants.stream()
                .filter(p -> !p.getId().equals(currentUserId))
                .findFirst()
                .map(ParticipantDTO::getFullName)
                .orElse("Direct Message");
        } else {
            // Group chat - show first few participants
            String names = participants.stream()
                .filter(p -> !p.getId().equals(currentUserId))
                .limit(3)
                .map(ParticipantDTO::getFirstname)
                .reduce((a, b) -> a + ", " + b)
                .orElse("Group Chat");
            
            if (participants.size() > 4) {
                names += " and " + (participants.size() - 4) + " others";
            }
            
            return names;
        }
    }
}

// ================================================================================================
