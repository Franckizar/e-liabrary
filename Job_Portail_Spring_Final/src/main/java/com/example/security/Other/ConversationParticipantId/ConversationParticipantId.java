// / 4. ConversationParticipantId.java (Composite Key) - FIXED
package com.example.security.Other.ConversationParticipantId;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationParticipantId implements Serializable {
    private Long conversation;
    private Long user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationParticipantId that = (ConversationParticipantId) o;
        return Objects.equals(conversation, that.conversation) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversation, user);
    }}