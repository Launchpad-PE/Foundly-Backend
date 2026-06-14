package com.foundly.foundlyplatform.comments.interfaces.rest.transform;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.interfaces.rest.resources.CommentResource;

/**
 * Assembler that converts {@link Comment} aggregates into REST {@link CommentResource} objects.
 */
public class CommentResourceFromEntityAssembler {

    /**
     * Converts a comment aggregate to its REST representation.
     *
     * @param comment comment aggregate
     * @return comment resource
     */
    public static CommentResource toResourceFromEntity(Comment comment) {
        return new CommentResource(
                comment.getId(),
                comment.getAuthorId(),
                comment.getTargetUserId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
