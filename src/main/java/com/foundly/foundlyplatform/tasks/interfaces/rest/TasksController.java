package com.foundly.foundlyplatform.tasks.interfaces.rest;

import com.foundly.foundlyplatform.iam.application.queryservices.UserQueryService;
import com.foundly.foundlyplatform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.foundly.foundlyplatform.shared.interfaces.rest.resources.MessageResource;
import com.foundly.foundlyplatform.tasks.application.commandservices.TaskCommandService;
import com.foundly.foundlyplatform.tasks.application.queryservices.TaskQueryService;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.commands.DeleteTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.commands.PatchTaskCommand;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByAssigneeIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectAndAssigneeQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.valueobjects.TaskStatus;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.CompleteTaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.CreateTaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.PatchTaskResource;
import com.foundly.foundlyplatform.tasks.interfaces.rest.resources.RescheduleTaskResource;
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

    // ═══════════════════════════════════════════════════════════════════
    // ✅ MÉTODOS DE PERMISOS - CORREGIDOS
    // ═══════════════════════════════════════════════════════════════════

    private boolean isOwner(Task task) {
        Long currentUserId = getCurrentUserId();
        String currentUserIdStr = String.valueOf(currentUserId);
        boolean result = task.getCreatorId() != null && task.getCreatorId().equals(currentUserIdStr);
        System.out.println("🔍 [isOwner] creatorId: " + task.getCreatorId() + " | currentUser: " + currentUserIdStr + " | result: " + result);
        return result;
    }

    private boolean isAssignee(Task task) {
        Long currentUserId = getCurrentUserId();
        String currentUserIdStr = String.valueOf(currentUserId);
        boolean result = task.getAssigneeId() != null && task.getAssigneeId().equals(currentUserIdStr);
        System.out.println("🔍 [isAssignee] assigneeId: " + task.getAssigneeId() + " | currentUser: " + currentUserIdStr + " | result: " + result);
        return result;
    }

    private boolean canView(Task task) {
        return isOwner(task) || isAssignee(task);
    }

    // ✅ ESTE ES EL MÉTODO QUE ESTABA FALTANDO - AHORA SÍ VERIFICA AL ASIGNADO
    private boolean canComplete(Task task) {
        Long currentUserId = getCurrentUserId();
        String currentUserIdStr = String.valueOf(currentUserId);

        boolean isOwnerResult = task.getCreatorId() != null && task.getCreatorId().equals(currentUserIdStr);
        boolean isAssigneeResult = task.getAssigneeId() != null && task.getAssigneeId().equals(currentUserIdStr);

        System.out.println("🔍 [canComplete] ======================================");
        System.out.println("🔍 [canComplete] creatorId: " + task.getCreatorId());
        System.out.println("🔍 [canComplete] assigneeId: " + task.getAssigneeId());
        System.out.println("🔍 [canComplete] currentUserId: " + currentUserIdStr);
        System.out.println("🔍 [canComplete] isOwner: " + isOwnerResult);
        System.out.println("🔍 [canComplete] isAssignee: " + isAssigneeResult);
        System.out.println("🔍 [canComplete] RESULTADO FINAL: " + (isOwnerResult || isAssigneeResult));
        System.out.println("🔍 [canComplete] ======================================");

        return isOwnerResult || isAssigneeResult;
    }

    private boolean canModify(Task task) {
        return isOwner(task);
    }

    private ResponseEntity<MessageResource> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MessageResource("No tienes permisos para realizar esta acción"));
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 CREATE - POST /api/v1/tasks
    // ═══════════════════════════════════════════════════════════════════

    @PostMapping
    @Operation(summary = "Create a task (emprendedor only)")
    public ResponseEntity<TaskResource> createTask(@RequestBody CreateTaskResource resource) {
        var command = CreateTaskCommandFromResourceAssembler.toCommandFromResource(
                resource, String.valueOf(getCurrentUserId()));
        var result = commandService.handle(command);
        return result
                .map(task -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.badRequest().build());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 GET BY ID - GET /api/v1/tasks/{id}
    // ═══════════════════════════════════════════════════════════════════

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        System.out.println("🔍 [BACKEND] getTaskById called with id: " + id);
        Optional<Task> taskOpt = queryService.handle(new GetTaskByIdQuery(id));
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!canView(taskOpt.get())) {
            return forbidden();
        }
        return ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(taskOpt.get()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 GET (QUERY PARAMS) - GET /api/v1/tasks?projectId=&assigneeId=
    // ═══════════════════════════════════════════════════════════════════

    @GetMapping
    public ResponseEntity<List<TaskResource>> getTasks(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String assigneeId) {

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

    // ═══════════════════════════════════════════════════════════════════
    // 📌 PATCH (EDITAR) - PATCH /api/v1/tasks/{id}
    // ═══════════════════════════════════════════════════════════════════

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchTask(
            @PathVariable Long id,
            @RequestBody PatchTaskResource resource) {
        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!canModify(existing.get())) {
            return forbidden();
        }

        var command = PatchTaskCommandFromResourceAssembler.toCommandFromResource(id, resource);
        return commandService.handle(command)
                .map(task -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 RESCHEDULE - PATCH /api/v1/tasks/{id}/due-date
    // ═══════════════════════════════════════════════════════════════════

    @PatchMapping("/{id}/due-date")
    public ResponseEntity<?> rescheduleTask(
            @PathVariable Long id,
            @RequestBody RescheduleTaskResource resource) {
        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!canModify(existing.get())) {
            return forbidden();
        }

        var command = new PatchTaskCommand(id, null, null, resource.newDueDate(),
                null, null, null, null, null, null, null);
        return commandService.handle(command)
                .map(task -> ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 COMPLETE - POST /api/v1/tasks/{id}/complete
    // ═══════════════════════════════════════════════════════════════════

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(
            @PathVariable Long id,
            @RequestBody CompleteTaskResource resource) {

        System.out.println("🔍 [BACKEND] completeTask called with id: " + id);

        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            System.out.println("❌ [BACKEND] Task not found");
            return ResponseEntity.notFound().build();
        }

        Task task = existing.get();
        Long currentUserId = getCurrentUserId();
        String currentUserIdStr = String.valueOf(currentUserId);

        // ✅ VERIFICACIÓN DIRECTA - SIN LLAMAR A OTROS MÉTODOS
        boolean isOwner = task.getCreatorId() != null && task.getCreatorId().equals(currentUserIdStr);
        boolean isAssignee = task.getAssigneeId() != null && task.getAssigneeId().equals(currentUserIdStr);

        System.out.println("🔍 [completeTask] ======================================");
        System.out.println("🔍 [completeTask] creatorId: " + task.getCreatorId());
        System.out.println("🔍 [completeTask] assigneeId: " + task.getAssigneeId());
        System.out.println("🔍 [completeTask] currentUserId: " + currentUserIdStr);
        System.out.println("🔍 [completeTask] isOwner: " + isOwner);
        System.out.println("🔍 [completeTask] isAssignee: " + isAssignee);
        System.out.println("🔍 [completeTask] PERMITIDO: " + (isOwner || isAssignee));
        System.out.println("🔍 [completeTask] ======================================");

        // ✅ PERMITIR si es el creador O el asignado
        if (!isOwner && !isAssignee) {
            System.out.println("❌ [completeTask] Usuario NO autorizado");
            return forbidden();
        }

        System.out.println("✅ [completeTask] Usuario autorizado, completando tarea...");

        var command = new PatchTaskCommand(id, null, null, null, null, null, null, null,
                TaskStatus.COMPLETED, resource.deliveryUrl(), resource.deliveryNotes());
        return commandService.handle(command)
                .map(updatedTask -> {
                    System.out.println("✅ [BACKEND] Task completed successfully: " + updatedTask.getId());
                    return ResponseEntity.ok(TaskResourceFromEntityAssembler.toResourceFromEntity(updatedTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 📌 DELETE - DELETE /api/v1/tasks/{id}
    // ═══════════════════════════════════════════════════════════════════

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        Optional<Task> existing = queryService.handle(new GetTaskByIdQuery(id));
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!canModify(existing.get())) {
            return forbidden();
        }

        commandService.handle(new DeleteTaskCommand(id));
        return ResponseEntity.noContent().build();
    }
}