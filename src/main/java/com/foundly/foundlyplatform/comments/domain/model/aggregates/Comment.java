package com.foundly.foundlyplatform.comments.domain.model.aggregates;

import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Comment aggregate root.
 *
 * <p>Represents a comment that one user (the author) leaves on another user's
 * profile (the target). Comments are direction-agnostic: any user can comment
 * on any other user's profile (e.g. entrepreneur to collaborator or vice versa).</p>
 */
@Getter
public class Comment extends AbstractDomainAggregateRoot<Comment> {

    @Setter
    private Long id;

    @Setter
    private Long authorId;

    @Setter
    private Long targetUserId;

    @Setter
    private String content;

    @Setter
    private Date createdAt;

    public Comment() {
    }

    public Comment(Long authorId, Long targetUserId, String content) {
        this.authorId = authorId;
        this.targetUserId = targetUserId;
        this.content = content;
    }
}
