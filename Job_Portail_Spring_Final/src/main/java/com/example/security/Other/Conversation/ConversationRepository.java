
// 7. ConversationRepository.java - ÉdiNova specific queries
package com.example.security.Other.Conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find a conversation that exists between exactly two community members.
     */
    @Query("""
        SELECT c FROM Conversation c 
        WHERE c.id IN (
            SELECT cp1.conversation.id FROM ConversationParticipant cp1 
            WHERE cp1.user.id = :user1Id
        )
        AND c.id IN (
            SELECT cp2.conversation.id FROM ConversationParticipant cp2 
            WHERE cp2.user.id = :user2Id
        )
        AND (
            SELECT COUNT(cp) FROM ConversationParticipant cp 
            WHERE cp.conversation.id = c.id
        ) = 2
    """)
    Optional<Conversation> findConversationBetweenTwoUsers(
        @Param("user1Id") Long user1Id,
        @Param("user2Id") Long user2Id
    );

    /**
     * Get all conversations for a specific community member, ordered by creation date.
     */
    @Query("""
        SELECT DISTINCT c FROM Conversation c
        JOIN c.participants cp
        WHERE cp.user.id = :userId
        ORDER BY c.createdAt DESC
    """)
    List<Conversation> findConversationsByUserId(@Param("userId") Long userId);

    /**
     * Find book club conversations by book ID
     */
    @Query("""
        SELECT c FROM Conversation c
        WHERE c.title LIKE CONCAT('%Book Club%')
        AND c.id IN (
            SELECT cp.conversation.id FROM ConversationParticipant cp
            WHERE cp.user.id = :userId
        )
        ORDER BY c.createdAt DESC
    """)
    List<Conversation> findBookClubConversationsByUserId(@Param("userId") Long userId);

    /**
     * Find conversations involving specific community members (group discussions)
     */
    @Query("""
        SELECT c FROM Conversation c
        WHERE c.id IN (
            SELECT cp.conversation.id FROM ConversationParticipant cp
            WHERE cp.user.id IN :userIds
            GROUP BY cp.conversation.id
            HAVING COUNT(DISTINCT cp.user.id) = :userCount
        )
        AND (
            SELECT COUNT(cp2) FROM ConversationParticipant cp2 
            WHERE cp2.conversation.id = c.id
        ) = :userCount
    """)
    List<Conversation> findConversationsByParticipants(
        @Param("userIds") List<Long> userIds,
        @Param("userCount") long userCount
    );

    /**
     * Find conversations with their participants for community member
     */
    @Query("""
        SELECT DISTINCT c FROM Conversation c
        LEFT JOIN FETCH c.participants cp
        LEFT JOIN FETCH cp.user
        WHERE c.id IN (
            SELECT cp2.conversation.id FROM ConversationParticipant cp2
            WHERE cp2.user.id = :userId
        )
        ORDER BY c.createdAt DESC
    """)
    List<Conversation> findConversationsByUserIdWithParticipants(@Param("userId") Long userId);

    /**
     * Search conversations by title for community features
     */
    @Query("""
        SELECT DISTINCT c FROM Conversation c
        JOIN c.participants cp
        WHERE cp.user.id = :userId
        AND (LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        ORDER BY c.createdAt DESC
    """)
    List<Conversation> searchConversationsByTitle(@Param("userId") Long userId, @Param("searchTerm") String searchTerm);
}

// ================================================================================================
