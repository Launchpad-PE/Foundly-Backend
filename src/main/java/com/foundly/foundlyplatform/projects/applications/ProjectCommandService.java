package com.foundly.foundlyplatform.projects.applications;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.commands.*;

import java.util.Optional;

public interface ProjectCommandService {
    Optional<Project> handle(CreateProjectCommand command);
    Optional<Project> handle(UpdateProjectCommand command);
    Optional<Project> handle(PatchProjectCommand command);
    Optional<Project> handle(PublishProjectCommand command);
    Optional<Project> handle(AddRoleCommand command);
    Optional<Project> handle(RemoveRoleCommand command);
}