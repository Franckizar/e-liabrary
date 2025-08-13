
// 6. ConversationController.java - ÉdiNova Community Chat
package com.example.security.Other.Conversation;

import com.example.security.Other.ConversationParticipant.ConversationParticipant;
import com.example.security.Other.Message.Message;
import com.example.security.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/community/conversations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConversationController {

    private final ConversationService conversationService;

    // --- DTOs for ÉdiNova Community ---
    public static record CreateConversationRequest(List<Long> userIds, String title) {}
    public static record SendMessageRequest(Long senderId, String messageText) {}
    public static record SendDirectMessageRequest(Long senderId, Long receiverId, String messageText) {}
    public static record CreateBookClubRequest(List<Long> userIds, String clubName, Long bookId) {}

    // --- Create a new conversation (readers/authors discussion) ---
    @PostMapping
    public ResponseEntity<?> createConversation(@RequestBody CreateConversationRequest request) {
        if (request.userIds() == null || request.userIds().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User IDs list is required for community conversation."));
        }
        try {
            Conversation conversation = conversationService.createConversation(request.userIds(), request.title());
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Create book club conversation ---
    @PostMapping("/book-club")
    public ResponseEntity<?> createBookClub(@RequestBody CreateBookClubRequest request) {
        if (request.userIds() == null || request.userIds().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User IDs list is required for book club."));
        }
        try {
            Conversation conversation = conversationService.createBookClubConversation(
                request.userIds(), request.clubName(), request.bookId());
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Add a reader/author to an existing conversation ---
    @PostMapping("/{conversationId}/participants/{userId}")
    public ResponseEntity<?> addParticipant(
            @PathVariable Long conversationId,
            @PathVariable Long userId) {
        try {
            ConversationParticipant participant = conversationService.addParticipant(conversationId, userId);
            return ResponseEntity.ok(participant);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Send a message in community conversation ---
    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long conversationId,
            @RequestBody SendMessageRequest request) {

        if (request.senderId() == null || request.messageText() == null || request.messageText().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "senderId and messageText are required."));
        }

        try {
            Message message = conversationService.sendMessage(conversationId, request.senderId(), request.messageText());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Send direct message between community members ---
    @PostMapping("/direct")
    public ResponseEntity<?> sendDirectMessage(@RequestBody SendDirectMessageRequest request) {
        if (request.senderId() == null || request.receiverId() == null ||
            request.messageText() == null || request.messageText().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "senderId, receiverId, and messageText are required."));
        }

        try {
            Message message = conversationService.sendDirectMessage(
                    request.senderId(), request.receiverId(), request.messageText());
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Get all messages from a community conversation ---
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
        try {
            List<Message> messages = conversationService.getMessages(conversationId);
            return ResponseEntity.ok(messages);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Community conversation not found"));
        }
    }

    // --- Get participants of a community conversation ---
    @GetMapping("/{conversationId}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long conversationId) {
        try {
            List<User> participants = conversationService.getParticipants(conversationId);
            return ResponseEntity.ok(participants);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Community conversation not found"));
        }
    }

    // --- Get all conversations for a community member ---
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserConversations(@PathVariable Long userId) {
        try {
            List<Conversation> conversations = conversationService.getUserConversations(userId);
            return ResponseEntity.ok(conversations);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // --- Get conversation by ID ---
    @GetMapping("/by-id/{conversationId}")
    public ResponseEntity<?> getConversationById(@PathVariable Long conversationId) {
        try {
            Conversation conversation = conversationService.getConversationById(conversationId);
            return ResponseEntity.ok(conversation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Community conversation not found"));
        }
    }
}

// ================================================================================================
