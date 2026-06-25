package com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.assemblers;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.infrastructure.persistence.jpa.entities.DirectMessagePersistenceEntity;

/**
 * Static assembler between direct message domain and persistence representations.
 */
public final class DirectMessagePersistenceAssembler {

    private DirectMessagePersistenceAssembler() {
    }

    public static DirectMessage toDomainFromPersistence(DirectMessagePersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new DirectMessage();
        domain.setId(entity.getId());
        domain.setSenderId(entity.getSenderId());
        domain.setRecipientId(entity.getRecipientId());
        domain.setContent(entity.getContent());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }

    public static DirectMessagePersistenceEntity toPersistenceFromDomain(DirectMessage message) {
        if (message == null) return null;
        var entity = new DirectMessagePersistenceEntity();
        if (message.getId() != null) {
            entity.setId(message.getId());
        }
        entity.setSenderId(message.getSenderId());
        entity.setRecipientId(message.getRecipientId());
        entity.setContent(message.getContent());
        return entity;
    }
}
