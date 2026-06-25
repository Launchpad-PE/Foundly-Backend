package com.foundly.foundlyplatform.messaging.domain.model.queries;

/**
 * Query to get every direct message involving a user (to build the conversation list).
 *
 * @param userId the user whose conversations are requested
 */
public record GetConversationsForUserQuery(Long userId) {
}
