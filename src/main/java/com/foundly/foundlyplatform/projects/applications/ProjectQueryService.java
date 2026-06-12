package com.foundly.foundlyplatform.projects.applications;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ProjectQueryService {
    Optional<Project> handle(GetProjectByIdQuery query);
    List<Project> handle(GetProjectsByAuthorIdQuery query);
    List<Project> handle(GetAllPublishedProjectsQuery query);
    List<Project> handle(GetProjectsByAreaQuery query);
}