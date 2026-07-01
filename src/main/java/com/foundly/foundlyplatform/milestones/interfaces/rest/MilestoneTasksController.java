package com.foundly.foundlyplatform.milestones.interfaces.rest;

import com.foundly.foundlyplatform.milestones.application.commandservices.MilestoneCommandService;
import com.foundly.foundlyplatform.milestones.application.queryservices.MilestoneQueryService;
import com.foundly.foundlyplatform.milestones.domain.model.commands.*;
import com.foundly.foundlyplatform.milestones.domain.model.queries.GetTaskByIdQuery;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.*;
import com.foundly.foundlyplatform.milestones.interfaces.rest.transform.MilestoneResourceFromEntityAssembler;
import com.foundly.foundlyplatform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Milestone Task management.
 *
 * <p>Endpoints:
 * GET    /api/v1/milestone-tasks/{id}               → get task by id
 * POST   /api/v1/milestones/{milestoneId}/tasks     → add task to milestone
 * PATCH  /api/v1/milestone-tasks/{id}/status        → update task status
 * POST   /api/v1/milestone-tasks/{id}/complete      → complete task with delivery
 * DELETE /api/v1/milestone-tasks/{id}               → delete task
 * </p>
 */
@RestController
@Tag(name = "Milestone Tasks", description = "Milestone Task Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MilestoneTasksController {

    private final MilestoneCommandService commandService;
    private final MilestoneQueryService queryService;

    public MilestoneTasksController(MilestoneCommandService commandService,
                                    MilestoneQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @GetMapping(value = "/api/v1/milestone-tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<MilestoneTaskResource> getTaskById(@PathVariable String id) {
        return queryService.handle(new GetTaskByIdQuery(id))
                .map(MilestoneResourceFromEntityAssembler::toTaskResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/api/v1/milestones/{milestoneId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add a task to a milestone")
    public ResponseEntity<?> addTaskToMilestone(
            @PathVariable String milestoneId,
            @RequestBody CreateTaskResource resource
    ) {
        List<ChecklistStep> checklist = resource.checklist() == null ? List.of() :
                resource.checklist().stream()
                        .map(s -> new ChecklistStep(s.description(), s.done()))
                        .toList();

        var command = new AddTaskToMilestoneCommand(
                milestoneId,
                resource.title(),
                resource.description(),
                resource.assigneeId(),
                checklist,
                resource.attachments() != null ? resource.attachments() : List.of()
        );

        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toTaskResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @PatchMapping(value = "/api/v1/milestone-tasks/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update task status (pending / completed / delayed)")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable String id,
            @RequestBody UpdateTaskStatusResource resource
    ) {
        var command = new UpdateTaskStatusCommand(id, resource.status());
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toTaskResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/v1/milestone-tasks/{id}/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Complete a task with a delivery URL")
    public ResponseEntity<?> completeTask(
            @PathVariable String id,
            @RequestBody CompleteTaskResource resource
    ) {
        var command = new CompleteTaskCommand(id, resource.deliveryUrl(), resource.deliveryNotes());
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toTaskResourceFromEntity,
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/api/v1/milestone-tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a task from its milestone")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        var result = commandService.handle(new DeleteTaskCommand(id));
        if (result.isSuccess()) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
