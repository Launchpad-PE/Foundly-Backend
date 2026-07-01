package com.foundly.foundlyplatform.messaging.domain.model.queries;

/**
 * Query to get the conversation (all direct messages) between two users.
 *
 * @param userAId one participant
 * @param userBId the other participant
 */
public record GetConversationQuery(Long userAId, Long userBId) {
}
