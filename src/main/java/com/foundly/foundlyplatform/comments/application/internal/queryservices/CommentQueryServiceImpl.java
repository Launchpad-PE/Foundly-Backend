package com.foundly.foundlyplatform.comments.application.internal.queryservices;

import com.foundly.foundlyplatform.comments.application.queryservices.CommentQueryService;
import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.domain.model.queries.GetCommentsByTargetUserIdQuery;
import com.foundly.foundlyplatform.comments.domain.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service that resolves comment read queries.
 */
@Service
public class CommentQueryServiceImpl implements CommentQueryService {

    private final CommentRepository commentRepository;

    public CommentQueryServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> handle(GetCommentsByTargetUserIdQuery query) {
        return commentRepository.findByTargetUserId(query.targetUserId());
    }
}
