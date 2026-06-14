package com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.entities.CommentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for comment persistence entities.
 */
@Repository
public interface CommentPersistenceRepository extends JpaRepository<CommentPersistenceEntity, Long> {

    /**
     * Finds the comments left on a user's profile, newest first.
     *
     * @param targetUserId identifier of the commented user
     * @return ordered list of comment persistence entities
     */
    List<CommentPersistenceEntity> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);
}
