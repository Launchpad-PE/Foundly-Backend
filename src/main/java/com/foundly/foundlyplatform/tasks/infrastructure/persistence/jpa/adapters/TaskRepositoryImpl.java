package com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.adapters;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.repositories.TaskRepository;
import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.assemblers.TaskPersistenceAssembler;
import com.foundly.foundlyplatform.tasks.infrastructure.persistence.jpa.repositories.TaskPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskPersistenceRepository persistence;

    public TaskRepositoryImpl(TaskPersistenceRepository persistence) {
        this.persistence = persistence;
    }

    @Override
    public Task save(Task task) {
        var entity = TaskPersistenceAssembler.toPersistenceEntity(task);
        var saved = persistence.save(entity);
        return TaskPersistenceAssembler.toDomainModel(saved);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return persistence.findById(id)
                .map(TaskPersistenceAssembler::toDomainModel);
    }

    @Override
    public List<Task> findByProjectId(String projectId) {
        return persistence.findByProjectId(projectId).stream()
                .map(TaskPersistenceAssembler::toDomainModel)
                .toList();
    }

    @Override
    public List<Task> findByAssigneeId(String assigneeId) {
        return persistence.findByAssigneeId(assigneeId).stream()
                .map(TaskPersistenceAssembler::toDomainModel)
                .toList();
    }

    @Override
    public List<Task> findByProjectIdAndAssigneeId(String projectId, String assigneeId) {
        return persistence.findByProjectIdAndAssigneeId(projectId, assigneeId).stream()
                .map(TaskPersistenceAssembler::toDomainModel)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        persistence.deleteById(id);
    }
}
