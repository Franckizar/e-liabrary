
// // Update ConversationService.java to include broadcasting
// package com.example.security.Other.Conversation;

// import com.example.security.UserRepository;
// import com.example.security.Other.ConversationParticipant.ConversationParticipant;
// import com.example.security.Other.ConversationParticipant.ConversationParticipantRepository;
// import com.example.security.Other.Message.ChatMessageDTO;
// import com.example.security.Other.Message.Message;
// import com.example.security.Other.Message.MessageBroadcastService;
// import com.example.security.Other.Message.MessageRepository;
// import com.example.security.Other.Message.MessageService;
// import com.example.security.user.User;

// import jakarta.transaction.Transactional;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional
// public class EnhancedConversationService extends ConversationService {

//     private final ConversationRepository conversationRepository;
//     private final ConversationParticipantRepository participantRepository;
//     private final MessageRepository messageRepository;
//     private final UserRepository userRepository;
//     private final MessageBroadcastService broadcastService;
//     private final MessageService messageService;

//     /**
//      * Send message with real-time broadcasting
//      */
//     @Override
//     public Message sendMessage(Long conversationId, Long senderId, String content) {
//         Message message = super.sendMessage(conversationId, senderId, content);
        
//         // Broadcast the message to all participants
//         ChatMessageDTO messageDTO = messageService.mapToDTO(message, senderId);
//         broadcastService.broadcastMessage(messageDTO);
        
//         return message;
//     }

//     /**
//      * Send direct message with real-time broadcasting
//      */
//     @Override
//     public Message sendDirectMessage(Long senderId, Long receiverId, String content) {
//         Message message = super.sendDirectMessage(senderId, receiverId, content);
        
//         // Broadcast the message
//         ChatMessageDTO messageDTO = messageService.mapToDTO(message, senderId);
//         broadcastService.broadcastMessage(messageDTO);
        
//         return message;
//     }

//     /**
//      * Get conversation summaries for a user (for conversation list)
//      */
//     public List<ConversationDTO> getConversationSummaries(Long userId) {
//         List<Conversation> conversations = conversationRepository.findConversationsByUserId(userId);
        
//         return conversations.stream()
//                 .map(conversation -> buildConversationDTO(conversation, userId))
//                 .collect(Collectors.toList());
//     }

//     /**
//      * Build ConversationDTO with summary information
//      */
//     private ConversationDTO buildConversationDTO(Conversation conversation, Long userId) {
//         // Get participants
//         List<ConversationParticipant> participants = participantRepository
//             .findByConversationIdWithUserDetails(conversation.getId());
        
//         List<ConversationDTO.ParticipantDTO> participantDTOs = participants.stream()
//                 .map(cp -> new ConversationDTO.ParticipantDTO(
//                     cp.getUser().getId(),
//                     cp.getUser().getFirstname(),
//                     cp.getUser().getLastname(),
//                     cp.getUser().getEmail(),
//                     cp.getJoinedAt()
//                 ))
//                 .collect(Collectors.toList());

//         // Get last message
//         List<Message> recentMessages = messageRepository
//             .findByConversationIdOrderByTimestampDesc(conversation.getId());
        
//         ChatMessageDTO lastMessageDTO = null;
//         if (!recentMessages.isEmpty()) {
//             Message lastMessage = recentMessages.get(0);
//             lastMessageDTO = messageService.mapToDTO(lastMessage, userId);
//         }

//         // Count unread messages
//         long unreadCount = messageRepository.findUnreadMessagesByConversationAndReceiver(
//             conversation.getId(), userId).size();

//         return ConversationDTO.builder()
//                 .id(conversation.getId())
//                 .createdAt(conversation.getCreatedAt())
//                 .isGroupChat(participants.size() > 2)
//                 .participantCount(participants.size())
//                 .participants(participantDTOs)
//                 .lastMessage(lastMessageDTO)
//                 .unreadCount(unreadCount)
//                 .lastActivity(lastMessageDTO != null ? lastMessageDTO.getTimestamp() : conversation.getCreatedAt())
//                 .build();
//     }

//     /**
//      * Add participant with notification
//      */
//     @Override
//     public ConversationParticipant addParticipant(Long conversationId, Long userId) {
//         ConversationParticipant participant = super.addParticipant(conversationId, userId);
        
//         // Notify all participants about new member
//         List<ConversationParticipant> allParticipants = participantRepository
//             .findByConversationId(conversationId);
        
//         allParticipants.forEach(cp -> 
//             broadcastService.notifyConversationUpdate(
//                 cp.getUser().getId(), 
//                 "PARTICIPANT_ADDED", 
//                 participant
//             )
//         );
        
//         return participant;
//     }

//     /**
//      * Remove participant with notification
//      */
//     @Override
//     public void removeParticipant(Long conversationId, Long userId) {
//         super.removeParticipant(conversationId, userId);
        
//         // Notify remaining participants
//         List<ConversationParticipant> remainingParticipants = participantRepository
//             .findByConversationId(conversationId);
        
//         remainingParticipants.forEach(cp -> 
//             broadcastService.notifyConversationUpdate(
//                 cp.getUser().getId(), 
//                 "PARTICIPANT_REMOVED", 
//                 userId
//             )
//         );
//     }

//     /**
//      * Update conversation title (for group chats)
//      */
//     public Conversation updateConversationTitle(Long conversationId, String title, Long userId) {
//         Conversation conversation = conversationRepository.findById(conversationId)
//                 .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

//         // Verify user is a participant
//         if (!participantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
//             throw new IllegalArgumentException("User is not a participant in this conversation");
//         }

//         conversation.setTitle(title);
//         conversation = conversationRepository.save(conversation);

//         // Notify all participants
//         List<ConversationParticipant> participants = participantRepository
//             .findByConversationId(conversationId);
        
//         participants.forEach(cp -> 
//             broadcastService.notifyConversationUpdate(
//                 cp.getUser().getId(), 
//                 "TITLE_UPDATED", 
//                 title
//             )
//         );

//         return conversation;
//     }

//     /**
//      * Search conversations by title or participant name
//      */
//     public List<ConversationDTO> searchConversations(Long userId, String query) {
//         // This is a simplified search - you might want to implement full-text search
//         List<Conversation> allConversations = conversationRepository.findConversationsByUserId(userId);
        
//         return allConversations.stream()
//                 .map(conversation -> buildConversationDTO(conversation, userId))
//                 .filter(dto -> {
//                     String title = dto.generateTitle(userId).toLowerCase();
//                     return title.contains(query.toLowerCase());
//                 })
//                 .collect(Collectors.toList());
//     }
// }