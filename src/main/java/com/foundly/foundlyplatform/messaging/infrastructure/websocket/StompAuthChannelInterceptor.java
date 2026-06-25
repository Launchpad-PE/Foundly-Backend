package com.foundly.foundlyplatform.messaging.infrastructure.websocket;

import com.foundly.foundlyplatform.iam.infrastructure.tokens.jwt.BearerTokenService;
import com.foundly.foundlyplatform.messaging.application.internal.outboundservices.acl.IamUserLookup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * Authenticates STOMP connections using the JWT sent in the CONNECT frame.
 *
 * <p>On CONNECT it reads the {@code Authorization: Bearer <token>} native header,
 * validates it with the existing {@link BearerTokenService}, resolves the user id
 * and binds it as the STOMP session principal. Subsequent frames inherit that user,
 * enabling per-user delivery via {@code /user/queue/...} destinations.</p>
 */
@Component
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final BearerTokenService tokenService;
    private final IamUserLookup userLookup;

    public StompAuthChannelInterceptor(BearerTokenService tokenService, IamUserLookup userLookup) {
        this.tokenService = tokenService;
        this.userLookup = userLookup;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        var token = extractToken(accessor.getFirstNativeHeader(AUTHORIZATION_HEADER));
        if (token == null || !tokenService.validateToken(token)) {
            log.warn("❌ WebSocket CONNECT sin token válido");
            return message;
        }

        var username = tokenService.getUsernameFromToken(token);
        var userId = userLookup.findUserIdByUsername(username);
        if (userId == null) {
            log.warn("❌ WebSocket CONNECT: usuario '{}' no encontrado", username);
            return message;
        }

        accessor.setUser(new StompPrincipal(String.valueOf(userId)));
        log.info("✅ WebSocket autenticado para userId={} ({})", userId, username);
        return message;
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
