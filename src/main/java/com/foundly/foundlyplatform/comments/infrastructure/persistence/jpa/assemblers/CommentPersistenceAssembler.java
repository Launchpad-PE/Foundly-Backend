package com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.entities.CommentPersistenceEntity;

/**
 * Static assembler between comment domain and persistence representations.
 */
public final class CommentPersistenceAssembler {

    private CommentPersistenceAssembler() {
    }

    public static Comment toDomainFromPersistence(CommentPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Comment();
        domain.setId(entity.getId());
        domain.setAuthorId(entity.getAuthorId());
        domain.setTargetUserId(entity.getTargetUserId());
        domain.setContent(entity.getContent());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }

    public static CommentPersistenceEntity toPersistenceFromDomain(Comment comment) {
        if (comment == null) return null;
        var entity = new CommentPersistenceEntity();
        if (comment.getId() != null) {
            entity.setId(comment.getId());
        }
        entity.setAuthorId(comment.getAuthorId());
        entity.setTargetUserId(comment.getTargetUserId());
        entity.setContent(comment.getContent());
        return entity;
    }
}
