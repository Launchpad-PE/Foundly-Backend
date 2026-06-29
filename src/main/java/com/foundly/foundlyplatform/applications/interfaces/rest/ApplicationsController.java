package com.foundly.foundlyplatform.applications.interfaces.rest;

import com.foundly.foundlyplatform.applications.application.commandservices.ApplicationCommandService;
import com.foundly.foundlyplatform.applications.application.queryservices.ApplicationQueryService;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationByIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectAndUserIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByProjectIdQuery;
import com.foundly.foundlyplatform.applications.domain.model.queries.GetApplicationsByUserIdQuery;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.ApplicationResource;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.CreateApplicationResource;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.UpdateApplicationStatusResource;
import com.foundly.foundlyplatform.applications.interfaces.rest.transform.ApplicationResourceFromEntityAssembler;
import com.foundly.foundlyplatform.applications.interfaces.rest.transform.CreateApplicationCommandFromResourceAssembler;
import com.foundly.foundlyplatform.applications.interfaces.rest.transform.UpdateApplicationStatusCommandFromResourceAssembler;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that exposes project application resources.
 *
 * <p>Endpoints summary:
 * <ul>
 *   <li>POST  /api/v1/applications                              – submit a new application</li>
 *   <li>GET   /api/v1/applications/{id}                        – get application by id</li>
 *   <li>GET   /api/v1/applications?projectId={id}              – list by project</li>
 *   <li>GET   /api/v1/applications?userId={id}                 – list by user</li>
 *   <li>GET   /api/v1/applications?projectId={id}&userId={id}  – list by project and user</li>
 *   <li>PATCH /api/v1/applications/{id}/status                 – accept or reject</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/applications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Applications", description = "Project application endpoints")
public class ApplicationsController {

    private final ApplicationCommandService commandService;
    private final ApplicationQueryService   queryService;

    public ApplicationsController(ApplicationCommandService commandService,
                                  ApplicationQueryService queryService) {
        this.commandService = commandService;
        this.queryService   = queryService;
    }

    // ── Commands ────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(
            summary  = "Submit an application to a project role",
            description = "Creates a new PENDING application for the specified user and role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application created",
                    content = @Content(schema = @Schema(implementation = ApplicationResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data or duplicate application"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> createApplication(@RequestBody CreateApplicationResource resource) {
        var command = CreateApplicationCommandFromResourceAssembler.toCommandFromResource(resource);
        var result  = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ApplicationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary  = "Accept or reject an application",
            description = "Updates the status of an existing application to ACCEPTED or REJECTED.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated",
                    content = @Content(schema = @Schema(implementation = ApplicationResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status or application ID"),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> updateStatus(
            @PathVariable @Parameter(description = "Application ID", example = "1") Long id,
            @RequestBody UpdateApplicationStatusResource resource
    ) {
        var command = UpdateApplicationStatusCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var result  = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ApplicationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    // ── Queries ─────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application found",
                    content = @Content(schema = @Schema(implementation = ApplicationResource.class))),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getById(
            @PathVariable @Parameter(description = "Application ID", example = "1") Long id
    ) {
        var query  = new GetApplicationByIdQuery(id);
        var result = queryService.handle(query);
        return result
                .map(ApplicationResourceFromEntityAssembler::toResourceFromEntity)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(
            summary = "List applications — filter by project, user, or both",
            description = """
                    Supported query parameters (at least one required):
                    - `projectId` → all applications for that project
                    - `userId`    → all applications by that user
                    - `projectId` + `userId` → applications matching both
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Applications retrieved"),
            @ApiResponse(responseCode = "400", description = "No filter parameter provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> list(
            @RequestParam(required = false) @Parameter(description = "Project ID filter") Long projectId,
            @RequestParam(required = false) @Parameter(description = "User ID filter")    Long userId
    ) {
        if (projectId != null && userId != null) {
            var applications = queryService.handle(new GetApplicationsByProjectAndUserIdQuery(projectId, userId));
            return ResponseEntity.ok(toResourceList(applications));
        }
        if (projectId != null) {
            var applications = queryService.handle(new GetApplicationsByProjectIdQuery(projectId));
            return ResponseEntity.ok(toResourceList(applications));
        }
        if (userId != null) {
            var applications = queryService.handle(new GetApplicationsByUserIdQuery(userId));
            return ResponseEntity.ok(toResourceList(applications));
        }
        return ResponseEntity.badRequest()
                .body("At least one query parameter (projectId or userId) is required.");
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private List<ApplicationResource> toResourceList(
            List<com.foundly.foundlyplatform.applications.domain.model.aggregates.Application> applications) {
        return applications.stream()
                .map(ApplicationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
