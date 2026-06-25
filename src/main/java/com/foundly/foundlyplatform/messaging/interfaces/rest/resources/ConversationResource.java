package com.foundly.foundlyplatform.messaging.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Summary of a 1-to-1 conversation (the other participant + the last message).
 */
@Schema(name = "ConversationResponse", description = "Summary of a conversation with another user")
public record ConversationResource(
        @Schema(description = "Identifier of the other participant", example = "2")
        Long otherUserId,

        @Schema(description = "Text of the most recent message", example = "Perfecto, lo reviso hoy")
        String lastMessage,

        @Schema(description = "Timestamp of the most recent message")
        Date lastMessageAt,

        @Schema(description = "True if the most recent message was sent by me", example = "false")
        boolean lastMessageFromMe
) {
}
