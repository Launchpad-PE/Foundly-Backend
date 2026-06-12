package com.foundly.foundlyplatform.projects.domain.services;

import com.foundly.foundlyplatform.projects.applications.ProjectQueryService;
import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.queries.GetProjectByIdQuery;

import java.util.Optional;

/**
 * ACL Facade for Project Management context.
 * Other bounded contexts can use this to interact with projects without
 * knowing internal details.
 */
public class ProjectContextFacade {

    private final ProjectQueryService projectQueryService;

    public ProjectContextFacade(ProjectQueryService projectQueryService) {
        this.projectQueryService = projectQueryService;
    }

    public Optional<Project> getProjectById(String projectId) {
        return projectQueryService.handle(new GetProjectByIdQuery(projectId));
    }

    public boolean projectExists(String projectId) {
        return getProjectById(projectId).isPresent();
    }

    public String getProjectName(String projectId) {
        return getProjectById(projectId)
                .map(Project::getNameValue)
                .orElse(null);
    }
}