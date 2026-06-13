package com.foundly.foundlyplatform.projects.interfaces.rest;

import com.foundly.foundlyplatform.iam.application.queryservices.UserQueryService;
import com.foundly.foundlyplatform.iam.domain.model.queries.GetUserByUsernameQuery;
import com.foundly.foundlyplatform.projects.applications.ProjectCommandService;
import com.foundly.foundlyplatform.projects.applications.ProjectQueryService;
import com.foundly.foundlyplatform.projects.domain.model.commands.*;
import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;
import com.foundly.foundlyplatform.projects.domain.model.queries.*;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.*;
import com.foundly.foundlyplatform.projects.interfaces.rest.resources.*;
import com.foundly.foundlyplatform.projects.interfaces.rest.transform.CreateProjectCommandFromResourceAssembler;
import com.foundly.foundlyplatform.projects.interfaces.rest.transform.ProjectResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Projects", description = "Project Management Endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProjectsController {

    private final ProjectCommandService projectCommandService;
    private final ProjectQueryService projectQueryService;
    private final UserQueryService userQueryService;

    public ProjectsController(ProjectCommandService projectCommandService,
                              ProjectQueryService projectQueryService,
                              UserQueryService userQueryService) {
        this.projectCommandService = projectCommandService;
        this.projectQueryService = projectQueryService;
        this.userQueryService = userQueryService;
    }

    private Long getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            var userQuery = new GetUserByUsernameQuery(username);
            var user = userQueryService.handle(userQuery);
            if (user.isPresent()) {
                return user.get().getId();
            }
        }
        throw new IllegalStateException("User not authenticated");
    }

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return null;
    }

    @GetMapping
    @Operation(summary = "Get all published projects")
    public ResponseEntity<List<ProjectResource>> getAllPublishedProjects() {
        var query = new GetAllPublishedProjectsQuery();
        var projects = projectQueryService.handle(query);
        var resources = projects.stream()
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's projects")
    public ResponseEntity<List<ProjectResource>> getMyProjects() {
        var userId = getCurrentUserId();
        var query = new GetProjectsByAuthorIdQuery(userId);
        var projects = projectQueryService.handle(query);
        var resources = projects.stream()
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<ProjectResource> getProjectById(@PathVariable String id) {
        var query = new GetProjectByIdQuery(id);
        return projectQueryService.handle(query)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/area/{area}")
    @Operation(summary = "Get projects by area")
    public ResponseEntity<List<ProjectResource>> getProjectsByArea(@PathVariable String area) {
        var query = new GetProjectsByAreaQuery(area);
        var projects = projectQueryService.handle(query);
        var resources = projects.stream()
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectResource> createProject(@Valid @RequestBody CreateProjectResource resource) {
        System.out.println("🔍 [CONTROLLER] environmental_impact recibido: " + resource.getEnvironmentalImpact());

        var userId = getCurrentUserId();
        var username = getCurrentUsername();
        var command = CreateProjectCommandFromResourceAssembler.toCommandFromResource(resource, userId, username);

        return projectCommandService.handle(command)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a project partially")
    public ResponseEntity<ProjectResource> patchProject(@PathVariable String id,
                                                        @RequestBody PatchProjectResource resource) {

        List<EnvironmentalMetric> environmentalMetrics = null;
        if (resource.environmentalMetrics() != null) {
            environmentalMetrics = resource.environmentalMetrics().stream()
                    .map(metricName -> {
                        try {
                            return EnvironmentalMetric.valueOf(metricName);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    })
                    .filter(metric -> metric != null)
                    .collect(Collectors.toList());
        }

        DurationType durationType = null;
        if (resource.durationType() != null) {
            try {
                durationType = DurationType.valueOf(resource.durationType().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        ProjectStatus status = null;
        if (resource.status() != null) {
            try {
                status = ProjectStatus.valueOf(resource.status().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        var command = new PatchProjectCommand(
                id,
                resource.name(),
                resource.area(),
                resource.tags(),
                resource.summary(),
                environmentalMetrics,
                resource.academicLevel(),
                resource.benefits(),
                resource.requiredSkills(),
                resource.durationAmount(),
                durationType,
                status
        );

        return projectCommandService.handle(command)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish a project")
    public ResponseEntity<ProjectResource> publishProject(@PathVariable String id) {
        var command = new PublishProjectCommand(id);
        return projectCommandService.handle(command)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/roles")
    @Operation(summary = "Add a role to a project")
    public ResponseEntity<ProjectResource> addRole(@PathVariable String id,
                                                   @RequestBody RoleResource resource) {
        var role = new ProjectRole(
                RoleName.of(resource.name()),
                CardTitle.of(resource.cardInfo().title()),
                resource.cardInfo().items().stream().map(CardItem::of).toList()
        );
        var command = new AddRoleCommand(id, role);
        return projectCommandService.handle(command)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Remove a role from a project")
    public ResponseEntity<ProjectResource> removeRole(@PathVariable String id,
                                                      @PathVariable Long roleId) {
        var command = new RemoveRoleCommand(id, roleId);
        return projectCommandService.handle(command)
                .map(ProjectResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}