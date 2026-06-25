package com.foundly.foundlyplatform.messaging.domain.repositories;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;

import java.util.List;

/**
 * Direct messages repository port.
 */
public interface DirectMessageRepository {

    DirectMessage save(DirectMessage message);

    /** Messages exchanged between two users, oldest first. */
    List<DirectMessage> findConversation(Long userAId, Long userBId);

    /** Every message where the user is sender or recipient, newest first. */
    List<DirectMessage> findAllForUser(Long userId);
}
