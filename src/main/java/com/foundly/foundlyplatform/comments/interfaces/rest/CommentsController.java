package com.foundly.foundlyplatform.comments.interfaces.rest;

import com.foundly.foundlyplatform.comments.application.commandservices.CommentCommandService;
import com.foundly.foundlyplatform.comments.application.queryservices.CommentQueryService;
import com.foundly.foundlyplatform.comments.domain.model.queries.GetCommentsByTargetUserIdQuery;
import com.foundly.foundlyplatform.comments.interfaces.rest.resources.CommentResource;
import com.foundly.foundlyplatform.comments.interfaces.rest.resources.CreateCommentResource;
import com.foundly.foundlyplatform.comments.interfaces.rest.transform.CommentResourceFromEntityAssembler;
import com.foundly.foundlyplatform.comments.interfaces.rest.transform.CreateCommentCommandFromResourceAssembler;
import com.foundly.foundlyplatform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes profile comment resources.
 */
@RestController
@RequestMapping(value = "/api/v1/users/{userId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Comments", description = "Profile comments endpoints")
public class CommentsController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    public CommentsController(CommentCommandService commentCommandService, CommentQueryService commentQueryService) {
        this.commentCommandService = commentCommandService;
        this.commentQueryService = commentQueryService;
    }

    /**
     * Leaves a comment on a user's profile.
     *
     * @param userId   identifier of the commented user (target)
     * @param resource create-comment request body
     * @return the created comment resource
     */
    @PostMapping
    @Operation(
            summary = "Leave a comment on a user's profile",
            description = "Creates a comment written by the author on the target user's profile.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = CommentResource.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid comment data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required or invalid")
    })
    public ResponseEntity<?> createComment(
            @PathVariable
            @Parameter(description = "Identifier of the commented user", example = "1", required = true)
            Long userId,
            @RequestBody CreateCommentResource resource
    ) {
        var command = CreateCommentCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var result = commentCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CommentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    /**
     * Retrieves the comments left on a user's profile.
     *
     * @param userId identifier of the commented user (target)
     * @return list of comment resources, newest first
     */
    @GetMapping
    @Operation(
            summary = "List comments for a user's profile",
            description = "Retrieves all comments left on the given user's profile, newest first.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CommentResource.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required or invalid")
    })
    public ResponseEntity<List<CommentResource>> getComments(
            @PathVariable
            @Parameter(description = "Identifier of the commented user", example = "1", required = true)
            Long userId
    ) {
        var query = new GetCommentsByTargetUserIdQuery(userId);
        var comments = commentQueryService.handle(query);
        var commentResources = comments.stream()
                .map(CommentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(commentResources);
    }
}
