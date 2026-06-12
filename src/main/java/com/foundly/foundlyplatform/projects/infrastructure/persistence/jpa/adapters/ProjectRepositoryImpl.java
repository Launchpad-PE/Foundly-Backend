package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.projects.domain.model.aggregates.Project;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;
import com.foundly.foundlyplatform.projects.domain.repositories.ProjectRepository;
import com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.assemblers.ProjectAssembler;
import com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.repositories.ProjectJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository jpaRepository;

    public ProjectRepositoryImpl(ProjectJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Project> findByProjectId(String projectId) {
        return jpaRepository.findByProjectId(projectId)
                .map(ProjectAssembler::toDomainFromJpa);
    }

    @Override
    public List<Project> findByAuthorId(Long authorId) {
        return jpaRepository.findByAuthorId(authorId).stream()
                .map(ProjectAssembler::toDomainFromJpa)
                .toList();
    }

    @Override
    public List<Project> findByStatus(ProjectStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(ProjectAssembler::toDomainFromJpa)
                .toList();
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProjectAssembler::toDomainFromJpa)
                .toList();
    }

    @Override
    public List<Project> findByArea(String area) {
        return jpaRepository.findByArea(area).stream()
                .map(ProjectAssembler::toDomainFromJpa)
                .toList();
    }

    @Override
    public Project save(Project project) {
        var entity = ProjectAssembler.toJpaFromDomain(project);
        var saved = jpaRepository.save(entity);
        return ProjectAssembler.toDomainFromJpa(saved);
    }

    @Override
    public void deleteByProjectId(String projectId) {
        jpaRepository.deleteByProjectId(projectId);
    }

    @Override
    public boolean existsByProjectId(String projectId) {
        return jpaRepository.existsByProjectId(projectId);
    }
}