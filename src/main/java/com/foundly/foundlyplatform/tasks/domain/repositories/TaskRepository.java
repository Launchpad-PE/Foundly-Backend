package com.foundly.foundlyplatform.tasks.domain.repositories;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(Long id);
    List<Task> findByProjectId(String projectId);
    List<Task> findByAssigneeId(String assigneeId);
    List<Task> findByProjectIdAndAssigneeId(String projectId, String assigneeId);
    void deleteById(Long id);
}
