package com.foundly.foundlyplatform.messaging.domain.model.aggregates;

import com.foundly.foundlyplatform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Direct message aggregate root.
 *
 * <p>A private 1-to-1 message from one user (sender) to another (recipient).
 * A "conversation" is the set of direct messages exchanged between two users.</p>
 */
@Getter
public class DirectMessage extends AbstractDomainAggregateRoot<DirectMessage> {

    @Setter
    private Long id;

    @Setter
    private Long senderId;

    @Setter
    private Long recipientId;

    @Setter
    private String content;

    @Setter
    private Date createdAt;

    public DirectMessage() {
    }

    public DirectMessage(Long senderId, Long recipientId, String content) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }
}
