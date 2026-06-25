package com.foundly.foundlyplatform.messaging.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to send a direct message. The sender is taken from the JWT.
 */
@Schema(
        name = "SendMessageRequest",
        description = "Request body to send a direct message to another user",
        example = "{\"recipientId\": 2, \"content\": \"Hola, ¿avanzamos con el proyecto?\"}"
)
public record SendMessageResource(
        @Schema(description = "Identifier of the user receiving the message", example = "2")
        Long recipientId,

        @Schema(description = "Message text", example = "Hola, ¿avanzamos con el proyecto?", maxLength = 2000)
        String content
) {
}
