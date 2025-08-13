// MessageBroadcastService.java
package com.example.security.Other.Message;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast new message to conversation participants
     */
    public void broadcastMessage(ChatMessageDTO message) {
        try {
            // Send to conversation topic
            messagingTemplate.convertAndSend(
                "/topic/conversation/" + message.getConversationId(), 
                message
            );
            
            // Send notification to receiver if it's a direct message
            if (message.getReceiver() != null) {
                messagingTemplate.convertAndSendToUser(
                    message.getReceiver().getId().toString(),
                    "/queue/messages",
                    message
                );
            }
            
            log.debug("Message broadcasted successfully for conversation: {}", message.getConversationId());
        } catch (Exception e) {
            log.error("Failed to broadcast message: {}", e.getMessage());
        }
    }

    /**
     * Notify user about conversation updates
     */
    public void notifyConversationUpdate(Long userId, String updateType, Object data) {
        try {
            messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/conversation-updates",
                new ConversationUpdateNotification(updateType, data)
            );
        } catch (Exception e) {
            log.error("Failed to send conversation update notification: {}", e.getMessage());
        }
    }

    /**
     * Notify about user online status
     */
    public void notifyUserOnlineStatus(Long userId, boolean isOnline) {
        try {
            messagingTemplate.convertAndSend(
                "/topic/user-status",
                new UserStatusNotification(userId, isOnline)
            );
        } catch (Exception e) {
            log.error("Failed to send user status notification: {}", e.getMessage());
        }
    }

    // Notification DTOs
    public record ConversationUpdateNotification(String type, Object data) {}
    public record UserStatusNotification(Long userId, boolean isOnline) {}
}
