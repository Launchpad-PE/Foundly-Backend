package com.foundly.foundlyplatform.tasks.application.internal.queryservices;

import com.foundly.foundlyplatform.tasks.application.queryservices.TaskQueryService;
import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByAssigneeIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectAndAssigneeQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectIdQuery;
import com.foundly.foundlyplatform.tasks.domain.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskQueryServiceImpl implements TaskQueryService {

    private final TaskRepository taskRepository;

    public TaskQueryServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Optional<Task> handle(GetTaskByIdQuery query) {
        return taskRepository.findById(query.id());
    }

    @Override
    public List<Task> handle(GetTasksByProjectIdQuery query) {
        return taskRepository.findByProjectId(query.projectId());
    }

    @Override
    public List<Task> handle(GetTasksByAssigneeIdQuery query) {
        return taskRepository.findByAssigneeId(query.assigneeId());
    }

    @Override
    public List<Task> handle(GetTasksByProjectAndAssigneeQuery query) {
        return taskRepository.findByProjectIdAndAssigneeId(query.projectId(), query.assigneeId());
    }
}
