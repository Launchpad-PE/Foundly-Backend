package com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.comments.domain.model.aggregates.Comment;
import com.foundly.foundlyplatform.comments.domain.repositories.CommentRepository;
import com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.assemblers.CommentPersistenceAssembler;
import com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.repositories.CommentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository adapter that bridges the comment domain repository port with Spring Data JPA.
 */
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentPersistenceRepository commentPersistenceRepository;

    public CommentRepositoryImpl(CommentPersistenceRepository commentPersistenceRepository) {
        this.commentPersistenceRepository = commentPersistenceRepository;
    }

    @Override
    public Comment save(Comment comment) {
        var saved = commentPersistenceRepository.save(CommentPersistenceAssembler.toPersistenceFromDomain(comment));
        return CommentPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<Comment> findByTargetUserId(Long targetUserId) {
        return commentPersistenceRepository.findByTargetUserIdOrderByCreatedAtDesc(targetUserId).stream()
                .map(CommentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
