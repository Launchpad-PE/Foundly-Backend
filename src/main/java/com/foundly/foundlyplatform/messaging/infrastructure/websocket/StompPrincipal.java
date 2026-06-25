package com.foundly.foundlyplatform.messaging.infrastructure.websocket;

import java.security.Principal;

/**
 * Minimal {@link Principal} whose name is the authenticated user's id.
 *
 * <p>Used as the STOMP session user so that {@code convertAndSendToUser(userId, ...)}
 * can route messages to the right subscriber on {@code /user/queue/messages}.</p>
 */
public record StompPrincipal(String name) implements Principal {

    @Override
    public String getName() {
        return name;
    }
}
