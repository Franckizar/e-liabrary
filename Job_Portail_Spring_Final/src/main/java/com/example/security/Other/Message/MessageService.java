
package com.example.security.Other.Message;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.security.Other.ConversationParticipant.ConversationParticipantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ConversationParticipantRepository participantRepository;

    /**
     * Get chat messages for a community conversation
     */
    public List<ChatMessageDTO> getChatMessages(Long conversationId, Long currentUserId) {
        // Verify the user is a participant
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, currentUserId)) {
            throw new IllegalArgumentException("User is not a participant in this community conversation");
        }

        List<Message> messages = messageRepository.findFullMessagesByConversation(conversationId);
        
        return messages.stream()
                .map(message -> mapToDTO(message, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * Mark messages as read for a user in a community conversation
     */
    public void markMessagesAsRead(Long conversationId, Long userId) {
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new IllegalArgumentException("User is not a participant in this community conversation");
        }

        List<Message> unreadMessages = messageRepository
            .findUnreadMessagesByConversationAndReceiver(conversationId, userId);
        
        unreadMessages.forEach(message -> message.setIsRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    /**
     * Get unread message count for a community member
     */
    public long getUnreadMessageCount(Long userId) {
        return messageRepository.countUnreadMessages(userId);
    }

    /**
     * Search messages by content in ÉdiNova community
     */
    public List<ChatMessageDTO> searchMessages(Long userId, String query, Long conversationId) {
        List<Message> messages = messageRepository.searchMessages(userId, query, conversationId);
        
        return messages.stream()
                .map(message -> mapToDTO(message, userId))
                .collect(Collectors.toList());
    }

    /**
     * Get recent messages for a community member
     */
    public List<ChatMessageDTO> getRecentMessages(Long userId, int limit) {
        List<Message> messages = messageRepository.findRecentMessagesForUser(userId, limit);
        
        return messages.stream()
                .map(message -> mapToDTO(message, userId))
                .collect(Collectors.toList());
    }

    /**
     * Get message count for a conversation
     */
    public long getMessageCount(Long conversationId) {
        return messageRepository.countByConversationId(conversationId);
    }

    /**
     * Map Message entity to ChatMessageDTO
     */
    public ChatMessageDTO mapToDTO(Message message, Long currentUserId) {
        boolean isFromCurrentUser = message.getSender().getId().equals(currentUserId);
        
        ChatMessageDTO.UserDTO senderDTO = new ChatMessageDTO.UserDTO(
            message.getSender().getId(),
            message.getSender().getFirstname(),
            message.getSender().getLastname(),
            message.getSender().getEmail()
        );

        ChatMessageDTO.UserDTO receiverDTO = null;
        if (message.getReceiver() != null) {
            receiverDTO = new ChatMessageDTO.UserDTO(
                message.getReceiver().getId(),
                message.getReceiver().getFirstname(),
                message.getReceiver().getLastname(),
                message.getReceiver().getEmail()
            );
        }

        return new ChatMessageDTO(
            message.getId(),
            message.getConversation().getId(),
            message.getContent(),
            message.getTimestamp(),
            message.getIsRead(),
            message.getMessageType(),
            isFromCurrentUser,
            senderDTO,
            receiverDTO
        );
    }

    /**
     * Delete a message (soft delete by marking as deleted)
     */
public void deleteMessage(Long messageId, Long userId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new IllegalArgumentException("Message not found"));

    // Only sender can delete their own messages
    if (!message.getSender().getId().equals(userId)) {
        throw new IllegalArgumentException("You can only delete your own messages");
    }

    message.setContent("[Message deleted]");
    message.setMessageType(Message.MessageType.DELETED);
    messageRepository.save(message);
}


    /**
     * Edit a message
     */
    public Message editMessage(Long messageId, Long userId, String newContent) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Message not found"));

        // Only sender can edit their own messages
        if (!message.getSender().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own messages");
        }

        // Don't allow editing of deleted messages
        if (message.getMessageType() == Message.MessageType.DELETED) {
            throw new IllegalArgumentException("Cannot edit deleted messages");
        }

        message.setContent(newContent);
        message.setMessageType(Message.MessageType.EDITED);
        
        return messageRepository.save(message);
    }

    /**
     * Get book club messages for ÉdiNova community
     */
    public List<ChatMessageDTO> getBookClubMessages(Long userId) {
        List<Message> messages = messageRepository.findBookClubMessages(userId);
        
        return messages.stream()
                .map(message -> mapToDTO(message, userId))
                .collect(Collectors.toList());
    }

    /**
     * Get conversation messages with pagination (for future implementation)
     */
    public List<ChatMessageDTO> getChatMessagesWithPagination(Long conversationId, Long currentUserId, 
                                                             int page, int size) {
        // Verify the user is a participant
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, currentUserId)) {
            throw new IllegalArgumentException("User is not a participant in this community conversation");
        }

        // For now, return all messages - implement pagination later if needed
        return getChatMessages(conversationId, currentUserId);
    }
}

// ============