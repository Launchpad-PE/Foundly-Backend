package com.foundly.foundlyplatform.projects.applications.internal.queryservices;


import com.foundly.foundlyplatform.projects.applications.ProjectQueryService;
import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.queries.*;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectQueryServiceImpl implements ProjectQueryService {

    private final ProjectRepository projectRepository;

    public ProjectQueryServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<Project> handle(GetProjectByIdQuery query) {
        return projectRepository.findByProjectId(query.projectId());
    }

    @Override
    public List<Project> handle(GetProjectsByAuthorIdQuery query) {
        return projectRepository.findByAuthorId(query.authorId());
    }

    @Override
    public List<Project> handle(GetAllPublishedProjectsQuery query) {
        return projectRepository.findByStatus(ProjectStatus.PUBLISHED);
    }

    @Override
    public List<Project> handle(GetProjectsByAreaQuery query) {
        return projectRepository.findByArea(query.area());
    }
}