package com.foundly.foundlyplatform.tasks.interfaces.rest;

import com.foundly.foundlyplatform.iam.application.queryservices.UserQueryService;
import com.foundly.foundlyplatform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.foundly.foundlyplatform.tasks.application.commandservices.TaskCommandService;
import com.foundly.foundlyplatform.tasks.application.queryservices.TaskQueryService;
import com.foundly.foundlyplatform.tasks.domain.model.commands.DeleteTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByAssigneeIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectAndAssigneeQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectIdQuery;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.CreateTaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.PatchTaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.TaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.transform.CreateTaskCommandFromResourceAssembler;
import com.foundly.foundlyplatform.tasks.interfaces.rest.transform.PatchTaskCommandFromResourceAssembler;
import com.foundly.foundlyplatform.tasks.interfaces.rest.transform.TaskResourceFromEntityAssembler;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for task management.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>POST   /api/v1/tasks                                      – create task</li>
 *   <li>GET    /api/v1/tasks/{id}                                 – get by id</li>
 *   <li>GET    /api/v1/tasks?projectId={id}                       – get by project</li>
 *   <li>GET    /api/v1/tasks?assigneeId={id}                      – get by assignee</li>
 *   <li>GET    /api/v1/tasks?projectId={id}&assigneeId={id}       – get by project and assignee</li>
 *   <li>PATCH  /api/v1/tasks/{id}                                 – partial update</li>
 *   <li>DELETE /api/v1/tasks/{id}                                 – delete task</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tasks", description = "Task management endpoints")
public class TasksController {

    private final TaskCommandService commandService;
    private final TaskQueryService queryService;
    private final UserQueryService userQueryService;

    public TasksController(TaskCommandService commandService, TaskQueryService queryService,
                            UserQueryService userQueryService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.userQueryService = userQueryService;
    }

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            var user = userQueryService.handle(new GetUserByUsernameQuery(auth.getName()));
            if (user.isPresent()) {
                return user.get().getId();
            }
        }
        throw new IllegalStateException("User not authenticated");
    }

    @PostMapping
    @Operation(summary = "Create a task", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created",
                    content = @Content(schema = @Schema(implementation = TaskResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskResource> createTask(@RequestBody CreateTaskResource resource) {
        // creatorId always comes from the authenticated user, never trusted from the request body
        var command = CreateTaskCommandFromResourceAssembler.toCommandFromResource(
                resource, String.valueOf(getCurrentUserId()));
        var result = commandService.handle(command);
        return result
                .map(task -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(schema = @Schema(implementation = TaskResource.class))),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskResource> getTaskById(
            @PathVariable @Parameter(description = "Task id", required = true) Long id) {
        var query = new GetTaskByIdQuery(id);
        return queryService.handle(query)
                .map(task -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get tasks by project, assignee, or both",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<TaskResource>> getTasks(
            @RequestParam(required = false) @Parameter(description = "Filter by project id") String projectId,
            @RequestParam(required = false) @Parameter(description = "Filter by assignee id") String assigneeId) {

        List<TaskResource> resources;

        if (projectId != null && assigneeId != null) {
            resources = queryService.handle(new GetTasksByProjectAndAssigneeQuery(projectId, assigneeId))
                    .stream().map(TaskResourceFromEntityAssembler::toResourceFromEntity).toList();
        } else if (projectId != null) {
            resources = queryService.handle(new GetTasksByProjectIdQuery(projectId))
                    .stream().map(TaskResourceFromEntityAssembler::toResourceFromEntity).toList();
        } else if (assigneeId != null) {
            resources = queryService.handle(new GetTasksByAssigneeIdQuery(assigneeId))
                    .stream().map(TaskResourceFromEntityAssembler::toResourceFromEntity).toList();
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a task", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(schema = @Schema(implementation = TaskResource.class))),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TaskResource> patchTask(
            @PathVariable @Parameter(description = "Task id", required = true) Long id,
            @RequestBody PatchTaskResource resource) {
        var command = PatchTaskCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return commandService.handle(command)
                .map(task -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteTask(
            @PathVariable @Parameter(description = "Task id", required = true) Long id) {
        commandService.handle(new DeleteTaskCommand(id));
        return ResponseEntity.noContent().build();
    }
}
