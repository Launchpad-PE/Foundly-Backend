package com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.entities;

import com.foundly.foundlyplatform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA persistence entity for direct messages.
 */
@Entity
@Table(name = "direct_messages", indexes = {
        @Index(name = "idx_dm_sender", columnList = "sender_id"),
        @Index(name = "idx_dm_recipient", columnList = "recipient_id")
})
@Getter
@Setter
@NoArgsConstructor
public class DirectMessagePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;
}
