package com.example.security.Other.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.example.security.Other.Message.Message.MessageType;

/**
 * DTO for chat message exchange in ÉdiNova community
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long messageId;
    private Long conversationId;
    private String content;
    private LocalDateTime timestamp;
    private Boolean isRead;
    private MessageType messageType;
    private boolean isFromCurrentUser;
    private UserDTO sender;
    private UserDTO receiver;
    private String senderName;
    private String receiverName;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private Long id;
        private String firstname;
        private String lastname;
        private String email;
        private String fullName;

        public UserDTO(Long id, String firstname, String lastname, String email) {
            this.id = id;
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.fullName = buildFullName(firstname, lastname);
        }

        private String buildFullName(String firstname, String lastname) {
            StringBuilder fullName = new StringBuilder();
            if (firstname != null && !firstname.trim().isEmpty()) {
                fullName.append(firstname.trim());
            }
            if (lastname != null && !lastname.trim().isEmpty()) {
                if (fullName.length() > 0) {
                    fullName.append(" ");
                }
                fullName.append(lastname.trim());
            }
            return fullName.toString();
        }

        public String getFullName() {
            if (fullName == null || fullName.trim().isEmpty()) {
                return buildFullName(firstname, lastname);
            }
            return fullName;
        }
    }

    // Constructor without receiver (for group messages)
    public ChatMessageDTO(Long messageId, Long conversationId, String content, LocalDateTime timestamp,
                         Boolean isRead, MessageType messageType, boolean isFromCurrentUser, UserDTO sender) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.messageType = messageType;
        this.isFromCurrentUser = isFromCurrentUser;
        this.sender = sender;
        this.receiver = null;
        this.senderName = sender != null ? sender.getFullName() : "";
        this.receiverName = "";
    }

    // Full constructor - FIXED
    public ChatMessageDTO(Long messageId, Long conversationId, String content, LocalDateTime timestamp,
                         Boolean isRead, MessageType messageType, boolean isFromCurrentUser, 
                         UserDTO sender, UserDTO receiver) {
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.messageType = messageType;
        this.isFromCurrentUser = isFromCurrentUser;
        this.sender = sender;
        this.receiver = receiver;
        this.senderName = sender != null ? sender.getFullName() : "";
        this.receiverName = receiver != null ? receiver.getFullName() : "";
    }
}

// ================================================================================================
