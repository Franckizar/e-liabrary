// ===============================================================================================

// 11. MessageRepository.java - Complete with all queries
package com.example.security.Other.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find messages by conversation ID ordered by timestamp
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp ASC")
    List<Message> findByConversationIdOrderByTimestampAsc(@Param("conversationId") Long conversationId);

    // Find messages by conversation ID ordered by timestamp (descending)
    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.timestamp DESC")
    List<Message> findByConversationIdOrderByTimestampDesc(@Param("conversationId") Long conversationId);

    // Find messages sent by a specific user
    @Query("SELECT m FROM Message m WHERE m.sender.id = :senderId ORDER BY m.timestamp DESC")
    List<Message> findBySenderIdOrderByTimestampDesc(@Param("senderId") Long senderId);

    // Find messages received by a specific user
    @Query("SELECT m FROM Message m WHERE m.receiver.id = :receiverId ORDER BY m.timestamp DESC")
    List<Message> findByReceiverIdOrderByTimestampDesc(@Param("receiverId") Long receiverId);

    // Find latest message in each conversation for a user
    @Query("""
        SELECT m FROM Message m 
        WHERE m.id IN (
            SELECT MAX(m2.id) FROM Message m2 
            WHERE m2.conversation.id IN (
                SELECT cp.conversation.id FROM ConversationParticipant cp 
                WHERE cp.user.id = :userId
            )
            GROUP BY m2.conversation.id
        )
        ORDER BY m.timestamp DESC
    """)
    List<Message> findLatestMessagesByUserId(@Param("userId") Long userId);

    // Count unread messages for a user
    @Query("""
        SELECT COUNT(m) FROM Message m 
        JOIN ConversationParticipant cp ON cp.conversation.id = m.conversation.id
        WHERE cp.user.id = :userId 
        AND m.sender.id != :userId 
        AND m.isRead = false
        AND m.messageType != 'DELETED'
    """)
    long countUnreadMessages(@Param("userId") Long userId);

    // Find conversation messages for user with full details
    @Query("""
        SELECT m FROM Message m 
        JOIN FETCH m.sender
        LEFT JOIN FETCH m.receiver
        WHERE m.conversation.id = :conversationId 
        AND EXISTS (
            SELECT 1 FROM ConversationParticipant cp 
            WHERE cp.conversation.id = :conversationId 
            AND cp.user.id = :userId
        )
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp ASC
    """)
    List<Message> findConversationMessagesForUser(
        @Param("conversationId") Long conversationId,
        @Param("userId") Long userId
    );

    // Find new messages since a specific time
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversation.id = :conversationId 
        AND m.timestamp > :since
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp ASC
    """)
    List<Message> findNewMessagesSince(
        @Param("conversationId") Long conversationId,
        @Param("since") LocalDateTime since
    );

    // Find full messages by conversation with all related data
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender
        LEFT JOIN FETCH m.receiver
        WHERE m.conversation.id = :conversationId
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp ASC
    """)
    List<Message> findFullMessagesByConversation(@Param("conversationId") Long conversationId);

    // Find unread messages by conversation and receiver
    @Query("""
        SELECT m FROM Message m
        WHERE m.conversation.id = :conversationId
        AND (m.receiver.id = :userId OR (m.receiver IS NULL AND m.sender.id != :userId))
        AND m.isRead = false
        AND m.messageType != 'DELETED'
    """)
    List<Message> findUnreadMessagesByConversationAndReceiver(
        @Param("conversationId") Long conversationId,
        @Param("userId") Long userId
    );

    // Mark messages as read
    @Modifying
    @Query("""
        UPDATE Message m SET m.isRead = true
        WHERE m.conversation.id = :conversationId
        AND (m.receiver.id = :userId OR (m.receiver IS NULL AND m.sender.id != :userId))
        AND m.isRead = false
    """)
    void markMessagesAsRead(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

    // Search messages by content in ÉdiNova community
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender
        LEFT JOIN FETCH m.receiver
        WHERE LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%'))
        AND EXISTS (
            SELECT 1 FROM ConversationParticipant cp 
            WHERE cp.conversation.id = m.conversation.id 
            AND cp.user.id = :userId
        )
        AND (:conversationId IS NULL OR m.conversation.id = :conversationId)
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp DESC
    """)
    List<Message> searchMessages(
        @Param("userId") Long userId,
        @Param("query") String query,
        @Param("conversationId") Long conversationId
    );

    // Get message count by conversation
    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :conversationId AND m.messageType != 'DELETED'")
    long countByConversationId(@Param("conversationId") Long conversationId);

    // Find recent messages for user
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender
        LEFT JOIN FETCH m.receiver
        WHERE EXISTS (
            SELECT 1 FROM ConversationParticipant cp 
            WHERE cp.conversation.id = m.conversation.id 
            AND cp.user.id = :userId
        )
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp DESC
    """)
    List<Message> findRecentMessagesForUser(@Param("userId") Long userId, @Param("limit") int limit);

    // Find messages by book club (for ÉdiNova specific feature)
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender
        WHERE m.conversation.title LIKE CONCAT('%Book Club%')
        AND EXISTS (
            SELECT 1 FROM ConversationParticipant cp 
            WHERE cp.conversation.id = m.conversation.id 
            AND cp.user.id = :userId
        )
        AND m.messageType != 'DELETED'
        ORDER BY m.timestamp DESC
    """)
    List<Message> findBookClubMessages(@Param("userId") Long userId);
}