package com.foundly.foundlyplatform.comments.domain.repositories;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;

import java.util.List;

/**
 * Comments repository port.
 */
public interface CommentRepository {

    Comment save(Comment comment);

    List<Comment> findByTargetUserId(Long targetUserId);
}
