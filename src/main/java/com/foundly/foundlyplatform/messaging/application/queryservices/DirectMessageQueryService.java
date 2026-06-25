package com.foundly.foundlyplatform.messaging.application.queryservices;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationQuery;
import com.foundly.foundlyplatform.messaging.domain.model.queries.GetConversationsForUserQuery;

import java.util.List;

/**
 * Application service contract for direct message read queries.
 */
public interface DirectMessageQueryService {

    /**
     * Returns all messages exchanged between two users, oldest first.
     */
    List<DirectMessage> handle(GetConversationQuery query);

    /**
     * Returns every message involving a user, newest first (to build the conversation list).
     */
    List<DirectMessage> handle(GetConversationsForUserQuery query);
}
