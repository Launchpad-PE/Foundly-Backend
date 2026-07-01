package com.foundly.foundlyplatform.messaging.interfaces.rest.transform;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.interfaces.rest.resources.DirectMessageResource;

/**
 * Assembler that converts {@link DirectMessage} aggregates into REST {@link DirectMessageResource} objects.
 */
public class DirectMessageResourceFromEntityAssembler {

    public static DirectMessageResource toResourceFromEntity(DirectMessage message) {
        return new DirectMessageResource(
                message.getId(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
