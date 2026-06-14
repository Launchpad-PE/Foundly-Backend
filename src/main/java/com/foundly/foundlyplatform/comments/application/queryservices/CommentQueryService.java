package com.foundly.foundlyplatform.comments.application.queryservices;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.domain.model.queries.GetCommentsByTargetUserIdQuery;

import java.util.List;

/**
 * Application service contract for comment read queries.
 */
public interface CommentQueryService {

    /**
     * Handles retrieval of all comments left on a user's profile.
     *
     * @param query target-user query
     * @return list of comments, newest first
     */
    List<Comment> handle(GetCommentsByTargetUserIdQuery query);
}
