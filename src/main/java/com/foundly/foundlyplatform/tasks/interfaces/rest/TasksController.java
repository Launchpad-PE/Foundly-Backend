package com.foundly.foundlyplatform.tasks.interfaces.rest;

import com.foundly.foundlyplatform.iam.application.queryservices.UserQueryService;
import com.foundly.foundlyplatform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.foundly.foundlyplatform.shared.interfaces.rest.resources.MessageResource;
import com.foundly.foundlyplatform.tasks.application.commandservices.TaskCommandService;
import com.foundly.foundlyplatform.tasks.application.queryservices.TaskQueryService;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
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
import java.util.Optional;

/**
 * REST controller for task management.
 *
 * <p>Endpoints (create/update/delete/view are restricted to the task's creator —
 * the emprendedor — so an empleado/colaborador can't be routed into an action
 * that isn't theirs, and vice versa):
 * <ul>
 *   <li>POST   /api/v1/tasks                                      – create task (emprendedor only)</li>
 *   <li>GET    /api/v1/tasks/{id}                                 – get by id (emprendedor/creator only)</li>
 *   <li>GET    /api/v1/tasks?projectId={id}                       – get by project</li>
 *   <li>GET    /api/v1/tasks?assigneeId={id}                      – get by assignee</li>
 *   <li>GET    /api/v1/tasks?projectId={id}&assigneeId={id}       – get by project and assignee</li>
 *   <li>PATCH  /api/v1/tasks/{id}                                 – partial update (emprendedor/creator only)</li>
 *   <li>DELETE /api/v1/tasks/{id}                                 – delete task (emprendedor/creator only)</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "bearerAuth")
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

    /**
     * Only the task's creator (the emprendedor who made it) may act on it.
     */
    private boolean isOwner(Task task) {
        return task.getCreatorId() != null
                && task.getCreatorId().equals(String.valueOf(getCurrentUserId()));
    }

    private ResponseEntity<MessageResource> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MessageResource("Only the task's creator (emprendedor) can perform this action"));
    }

    @PostMapping
    @Operation(summary = "Create a task (emprendedor only)")
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
    @Operation(summary = "Get task by id (emprendedor/creator only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(schema = @Schema(implementation = TaskResource.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getTaskById(
            @PathVariable @Parameter(description = "Task id", required = true) Long id) {
        Optional<Task> taskOpt = queryService.handle(new GetTaskByIdQuery(id));
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!isOwner(taskOpt.get())) {
            return forbidden();
        }
        return ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(taskOpt.get()));
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
    @Operation(summary = "Partially update a task (emprendedor/creator only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(schema = @Schema(implementation = TaskResource.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> patchTask(
            @PathVariable @Parameter(description = "Task id", required = true) Long id,
            @RequestBody PatchTaskResource resource) {
        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!isOwner(existing.get())) {
            return forbidden();
        }

        var command = PatchTaskCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return commandService.handle(command)
                .map(task -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task (emprendedor/creator only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> deleteTask(
            @PathVariable @Parameter(description = "Task id", required = true) Long id) {
        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!isOwner(existing.get())) {
            return forbidden();
        }

        commandService.handle(new DeleteTaskCommand(id));
        return ResponseEntity.noContent().build();
    }
}
