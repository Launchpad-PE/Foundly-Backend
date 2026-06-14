package com.foundly.foundlyplatform.comments.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for profile comments.
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class CommentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "target_user_id", nullable = false)
    private Long targetUserId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;
}
