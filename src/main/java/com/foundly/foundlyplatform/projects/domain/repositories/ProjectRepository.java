package com.foundly.foundlyplatform.projects.domain.repositories;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findByProjectId(String projectId);
    List<Project> findByAuthorId(Long authorId);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findAll();
    List<Project> findByArea(String area);
    Project save(Project project);
    void deleteByProjectId(String projectId);
    boolean existsByProjectId(String projectId);
}