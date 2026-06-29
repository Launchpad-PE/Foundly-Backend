package com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.repositories;

import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.entities.TaskPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskPersistenceRepository extends JpaRepository<TaskPersistenceEntity, Long> {
    List<TaskPersistenceEntity> findByProjectId(String projectId);
    List<TaskPersistenceEntity> findByAssigneeId(String assigneeId);
    List<TaskPersistenceEntity> findByProjectIdAndAssigneeId(String projectId, String assigneeId);
}
