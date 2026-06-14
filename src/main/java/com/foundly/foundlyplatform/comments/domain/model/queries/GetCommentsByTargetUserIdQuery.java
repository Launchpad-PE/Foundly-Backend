package com.foundly.foundlyplatform.comments.domain.model.queries;

/**
 * Query to get all comments left on a user's profile.
 *
 * @param targetUserId identifier of the user whose profile comments are requested
 */
public record GetCommentsByTargetUserIdQuery(Long targetUserId) {
}
