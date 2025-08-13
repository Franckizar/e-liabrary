package com.example.security.Other.Message;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth/community/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    /**
     * Get chat messages for a community conversation
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<?> getChatMessages(
            @PathVariable Long conversationId,
            @RequestParam Long userId) {
        try {
            List<ChatMessageDTO> messages = messageService.getChatMessages(conversationId, userId);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to retrieve community messages"));
        }
    }

    /**
     * Mark messages as read in community conversation
     */
    @PutMapping("/conversations/{conversationId}/read")
    public ResponseEntity<?> markMessagesAsRead(
            @PathVariable Long conversationId,
            @RequestParam Long userId) {
        try {
            messageService.markMessagesAsRead(conversationId, userId);
            return ResponseEntity.ok(Map.of("message", "Community messages marked as read"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to mark community messages as read"));
        }
    }

    /**
     * Get unread message count for community member
     */
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<?> getUnreadMessageCount(@PathVariable Long userId) {
        try {
            long count = messageService.getUnreadMessageCount(userId);
            return ResponseEntity.ok(Map.of("unreadCount", count));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to get unread count"));
        }
    }

    /**
     * Search messages in ÉdiNova community
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchMessages(
            @RequestParam Long userId,
            @RequestParam String query,
            @RequestParam(required = false) Long conversationId) {
        try {
            List<ChatMessageDTO> messages = messageService.searchMessages(userId, query, conversationId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Community message search failed"));
        }
    }

    /**
     * Edit a message in community conversation
     */
    @PutMapping("/{messageId}")
    public ResponseEntity<?> editMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId,
            @RequestBody Map<String, String> request) {
        try {
            String newContent = request.get("content");
            if (newContent == null || newContent.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Message content is required"));
            }
            
            Message message = messageService.editMessage(messageId, userId, newContent);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to edit message"));
        }
    }

    /**
     * Delete a message in community conversation
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam Long userId) {
        try {
            messageService.deleteMessage(messageId, userId);
            return ResponseEntity.ok(Map.of("message", "Community message deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to delete message"));
        }
    }
}

// ==========