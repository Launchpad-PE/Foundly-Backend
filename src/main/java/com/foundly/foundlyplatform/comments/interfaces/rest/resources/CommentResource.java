package com.foundly.foundlyplatform.comments.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * Resource representing a comment returned by the REST API.
 */
@Schema(
        name = "CommentResponse",
        description = "A comment left on a user's profile"
)
public record CommentResource(
        @Schema(description = "Comment unique identifier", example = "1")
        Long id,

        @Schema(description = "Identifier of the user who wrote the comment", example = "2")
        Long authorId,

        @Schema(description = "Identifier of the user whose profile was commented", example = "1")
        Long targetUserId,

        @Schema(description = "Comment text", example = "¡Excelente colaborador, muy recomendado!")
        String content,

        @Schema(description = "Creation timestamp")
        Date createdAt
) {
}
