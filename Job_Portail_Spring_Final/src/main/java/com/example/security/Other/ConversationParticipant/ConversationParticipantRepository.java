package com.example.security.Other.ConversationParticipant;

import com.example.security.Other.ConversationParticipantId.ConversationParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipantId> {

    // Find all participants in a community conversation
    @Query("SELECT cp FROM ConversationParticipant cp JOIN FETCH cp.user WHERE cp.conversation.id = :conversationId")
    List<ConversationParticipant> findByConversationId(@Param("conversationId") Long conversationId);

    // Find all conversations a community member participates in
    @Query("SELECT cp FROM ConversationParticipant cp JOIN FETCH cp.conversation WHERE cp.user.id = :userId")
    List<ConversationParticipant> findByUserId(@Param("userId") Long userId);

    // Check if user is participant in conversation
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    // Count participants in a conversation
    long countByConversationId(Long conversationId);

    // Find participants excluding a specific user (useful for getting "other" participants)
    @Query("SELECT cp FROM ConversationParticipant cp JOIN FETCH cp.user WHERE cp.conversation.id = :conversationId AND cp.user.id != :excludeUserId")
    List<ConversationParticipant> findByConversationIdAndUserIdNot(
        @Param("conversationId") Long conversationId, 
        @Param("excludeUserId") Long excludeUserId
    );

    // Remove participant from conversation
    @Modifying
    @Query("DELETE FROM ConversationParticipant cp WHERE cp.conversation.id = :conversationId AND cp.user.id = :userId")
    void deleteByConversationIdAndUserId(@Param("conversationId") Long conversationId, @Param("userId") Long userId);

    // Find conversations between specific users
    @Query("""
        SELECT DISTINCT cp1 FROM ConversationParticipant cp1 
        JOIN ConversationParticipant cp2 ON cp1.conversation.id = cp2.conversation.id 
        WHERE cp1.user.id = :user1Id AND cp2.user.id = :user2Id
        AND cp1.user.id != cp2.user.id
    """)
    List<ConversationParticipant> findConversationsBetweenUsers(
        @Param("user1Id") Long user1Id, 
        @Param("user2Id") Long user2Id
    );

    // Find participants with user details
    @Query("""
        SELECT cp FROM ConversationParticipant cp 
        JOIN FETCH cp.user u 
        WHERE cp.conversation.id = :conversationId 
        ORDER BY u.firstname, u.lastname
    """)
    List<ConversationParticipant> findByConversationIdWithUserDetails(@Param("conversationId") Long conversationId);

    // Find book club admins
    @Query("""
        SELECT cp FROM ConversationParticipant cp 
        JOIN FETCH cp.user u 
        WHERE cp.conversation.id = :conversationId 
        AND cp.isAdmin = true
    """)
    List<ConversationParticipant> findAdminsByConversationId(@Param("conversationId") Long conversationId);
}