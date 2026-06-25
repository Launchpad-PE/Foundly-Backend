package com.foundly.foundlyplatform.messaging.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Resource representing a direct message. Also the payload pushed over WebSocket.
 */
@Schema(name = "DirectMessageResponse", description = "A direct (1-to-1) message")
public record DirectMessageResource(
        @Schema(description = "Message unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the sender", example = "1")
        Long senderId,

        @Schema(description = "Identifier of the recipient", example = "2")
        Long recipientId,

        @Schema(description = "Message text", example = "Hola, ¿avanzamos con el proyecto?")
        String content,

        @Schema(description = "Creation timestamp")
        Date createdAt
) {
}
