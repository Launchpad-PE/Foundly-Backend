package com.foundly.foundlyplatform.comments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to leave a comment on a user's profile.
 */
@Schema(
        name = "CreateCommentRequest",
        description = "Request body to leave a comment on a user's profile",
        example = "{\"authorId\": 2, \"content\": \"¡Excelente colaborador, muy recomendado!\"}"
)
public record CreateCommentResource(
        @Schema(description = "Identifier of the user writing the comment", example = "2")
        Long authorId,

        @Schema(description = "Comment text", example = "¡Excelente colaborador, muy recomendado!", maxLength = 1000)
        String content
) {
}
