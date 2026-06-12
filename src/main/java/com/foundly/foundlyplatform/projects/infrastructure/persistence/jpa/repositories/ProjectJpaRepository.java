package com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;
import com.foundly.foundlyplatform.projects.infrastructure.persistence.jpa.entities.ProjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, Long> {
    Optional<ProjectJpaEntity> findByProjectId(String projectId);
    List<ProjectJpaEntity> findByAuthorId(Long authorId);
    List<ProjectJpaEntity> findByStatus(ProjectStatus status);
    List<ProjectJpaEntity> findByArea(String area);
    boolean existsByProjectId(String projectId);
    void deleteByProjectId(String projectId);
}