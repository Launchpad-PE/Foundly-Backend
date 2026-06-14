package com.foundly.foundlyplatform.comments.domain.model.commands;

/**
 * Command to create a comment on a user's profile.
 *
 * @param authorId     identifier of the user writing the comment
 * @param targetUserId identifier of the user whose profile is being commented
 * @param content      the comment text
 *
 * @see com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment
 */
public record CreateCommentCommand(Long authorId, Long targetUserId, String content) {
}
