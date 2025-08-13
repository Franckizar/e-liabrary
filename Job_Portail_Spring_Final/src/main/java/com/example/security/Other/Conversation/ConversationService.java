
// 8. ConversationService.java - ÉdiNova Community Service
package com.example.security.Other.Conversation;

import com.example.security.UserRepository;
import com.example.security.Other.ConversationParticipant.ConversationParticipant;
import com.example.security.Other.ConversationParticipant.ConversationParticipantRepository;
import com.example.security.Other.Message.Message;
import com.example.security.Other.Message.MessageRepository;
import com.example.security.user.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * Create a new conversation for ÉdiNova community members
     */
    public Conversation createConversation(List<Long> userIds, String title) {
        // Validate that all user IDs exist
        for (Long userId : userIds) {
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("Community member not found with ID: " + userId);
            }
        }

        // Check if conversation already exists for these exact participants
        List<Conversation> existingConversations = conversationRepository
            .findConversationsByParticipants(userIds, userIds.size());
        
        if (!existingConversations.isEmpty()) {
            return existingConversations.get(0); // Return existing conversation
        }

        // Create new conversation
        Conversation conversation = Conversation.builder()
            .title(title)
            .createdAt(LocalDateTime.now())
            .isGroupChat(userIds.size() > 2)
            .build();
        
        conversation = conversationRepository.save(conversation);

        // Add participants
        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Community member not found with ID: " + userId));

            ConversationParticipant participant = ConversationParticipant.builder()
                    .conversation(conversation)
                    .user(user)
                    .joinedAt(LocalDateTime.now())
                    .isAdmin(false)
                    .build();

            participantRepository.save(participant);
        }

        return conversationRepository.findById(conversation.getId()).orElse(conversation);
    }

    /**
     * Create a book club conversation
     */
    public Conversation createBookClubConversation(List<Long> userIds, String clubName, Long bookId) {
        String title = "Book Club: " + clubName + " (Book ID: " + bookId + ")";
        return createConversation(userIds, title);
    }

    /**
     * Add participant to existing community conversation
     */
    public ConversationParticipant addParticipant(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Community conversation not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Community member not found"));

        // Check if user is already a participant
        if (participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new IllegalStateException("User is already a participant in this community conversation");
        }

        ConversationParticipant participant = ConversationParticipant.builder()
                .conversation(conversation)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .isAdmin(false)
                .build();

        return participantRepository.save(participant);
    }

    /**
     * Send a message in existing community conversation
     */
    public Message sendMessage(Long conversationId, Long senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Community conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        // Validate that sender is a participant
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, senderId)) {
            throw new IllegalArgumentException("Sender is not a participant in this community conversation");
        }

        Message message = Message.builder()
                .content(content)
                .conversation(conversation)
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .messageType(Message.MessageType.TEXT)
                .build();

        return messageRepository.save(message);
    }

    /**
     * Send direct message between two community members
     */
    public Message sendDirectMessage(Long senderId, Long receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // Find or create conversation between two community members
        Optional<Conversation> existingConversationOpt = conversationRepository
            .findConversationBetweenTwoUsers(senderId, receiverId);
        
        Conversation conversation;
        if (existingConversationOpt.isPresent()) {
            conversation = existingConversationOpt.get();
        } else {
            // Create new direct conversation
            conversation = createConversation(List.of(senderId, receiverId), "Direct Message");
        }

        Message message = Message.builder()
                .content(content)
                .conversation(conversation)
                .sender(sender)
                .receiver(receiver)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .messageType(Message.MessageType.TEXT)
                .build();

        return messageRepository.save(message);
    }

    /**
     * Get all messages for a community conversation
     */
    public List<Message> getMessages(Long conversationId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new IllegalArgumentException("Community conversation not found");
        }
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
    }

    /**
     * Get participants of a community conversation
     */
    public List<User> getParticipants(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Community conversation not found"));
        
        return participantRepository.findByConversationId(conversationId)
                .stream()
                .map(ConversationParticipant::getUser)
                .toList();
    }

    /**
     * Get all conversations for a community member
     */
    public List<Conversation> getUserConversations(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Community member not found");
        }
        return conversationRepository.findConversationsByUserId(userId);
    }

    /**
     * Get conversation by ID
     */
    public Conversation getConversationById(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Community conversation not found"));
    }

    /**
     * Remove participant from conversation
     */
    public void removeParticipant(Long conversationId, Long userId) {
        if (!conversationRepository.existsById(conversationId)) {
            throw new IllegalArgumentException("Community conversation not found");
        }
        
        if (!participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new IllegalArgumentException("User is not a participant in this community conversation");
        }

        participantRepository.deleteByConversationIdAndUserId(conversationId, userId);
    }

    /**
     * Mark messages as read in community conversation
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
     * Search conversations by title
     */
    public List<Conversation> searchConversations(Long userId, String searchTerm) {
        return conversationRepository.searchConversationsByTitle(userId, searchTerm);
    }

    /**
     * Get book club conversations for a user
     */
    public List<Conversation> getBookClubConversations(Long userId) {
        return conversationRepository.findBookClubConversationsByUserId(userId);
    }
}