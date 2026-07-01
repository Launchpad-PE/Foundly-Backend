package com.foundly.foundlyplatform.milestones.interfaces.rest;

import com.foundly.foundlyplatform.milestones.application.commandservices.MilestoneCommandService;
import com.foundly.foundlyplatform.milestones.application.queryservices.MilestoneQueryService;
import com.foundly.foundlyplatform.milestones.domain.model.commands.*;
import com.foundly.foundlyplatform.milestones.domain.model.queries.*;
import com.foundly.foundlyplatform.milestones.interfaces.rest.resources.*;
import com.foundly.foundlyplatform.milestones.interfaces.rest.transform.CreateMilestoneCommandFromResourceAssembler;
import com.foundly.foundlyplatform.milestones.interfaces.rest.transform.MilestoneResourceFromEntityAssembler;
import com.foundly.foundlyplatform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * REST controller for Milestone management.
 *
 * <p>Endpoints:
 * GET    /api/v1/milestones?projectId=...  → get all milestones for a project
 * GET    /api/v1/milestones/{id}           → get milestone by id
 * POST   /api/v1/milestones               → create milestone (with optional tasks)
 * PUT    /api/v1/milestones/{id}           → full update
 * PATCH  /api/v1/milestones/{id}           → partial update
 * PATCH  /api/v1/milestones/{id}/reschedule → reschedule due date
 * DELETE /api/v1/milestones/{id}           → delete milestone
 * </p>
 */
@RestController
@RequestMapping(value = "/api/v1/milestones", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Milestones", description = "Milestone Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MilestonesController {

    private final MilestoneCommandService commandService;
    private final MilestoneQueryService queryService;

    public MilestonesController(MilestoneCommandService commandService,
                                MilestoneQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // ─── Queries ──────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get milestones by project")
    public ResponseEntity<List<MilestoneResource>> getMilestonesByProject(
            @RequestParam String projectId
    ) {
        var milestones = queryService.handle(new GetMilestonesByProjectIdQuery(projectId));
        var resources = milestones.stream()
                .map(MilestoneResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get milestone by ID")
    public ResponseEntity<MilestoneResource> getMilestoneById(@PathVariable String id) {
        return queryService.handle(new GetMilestoneByIdQuery(id))
                .map(MilestoneResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── Commands ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new milestone (with optional initial tasks)")
    public ResponseEntity<?> createMilestone(@Valid @RequestBody CreateMilestoneResource resource) {
        var command = CreateMilestoneCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a milestone (full update)")
    public ResponseEntity<?> updateMilestone(
            @PathVariable String id,
            @RequestBody UpdateMilestoneResource resource
    ) {
        var command = new UpdateMilestoneCommand(
                id,
                resource.title(),
                resource.description(),
                resource.dueDate(),
                resource.tools(),
                resource.generalComment(),
                resource.attachments()
        );
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a milestone")
    public ResponseEntity<?> patchMilestone(
            @PathVariable String id,
            @RequestBody UpdateMilestoneResource resource
    ) {
        var command = new UpdateMilestoneCommand(
                id,
                resource.title(),
                resource.description(),
                resource.dueDate(),
                resource.tools(),
                resource.generalComment(),
                resource.attachments()
        );
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @PatchMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule a milestone's due date")
    public ResponseEntity<?> rescheduleMilestone(
            @PathVariable String id,
            @RequestBody java.util.Map<String, Object> body
    ) {
        Date newDueDate;
        try {
            var dateStr = body.get("dueDate").toString();
            newDueDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid dueDate format. Use yyyy-MM-dd.");
        }

        var command = new RescheduleMilestoneCommand(id, newDueDate);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                MilestoneResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a milestone")
    public ResponseEntity<?> deleteMilestone(@PathVariable String id) {
        var command = new DeleteMilestoneCommand(id);
        var result = commandService.handle(command);
        if (result.isSuccess()) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
