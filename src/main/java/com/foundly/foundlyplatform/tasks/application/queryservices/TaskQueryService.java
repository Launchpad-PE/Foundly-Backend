package com.foundly.foundlyplatform.tasks.application.queryservices;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTaskByIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByAssigneeIdQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectAndAssigneeQuery;
import com.foundly.foundlyplatform.tasks.domain.model.queries.GetTasksByProjectIdQuery;

import java.util.List;
import java.util.Optional;

public interface TaskQueryService {
    Optional<Task> handle(GetTaskByIdQuery query);
    List<Task> handle(GetTasksByProjectIdQuery query);
    List<Task> handle(GetTasksByAssigneeIdQuery query);
    List<Task> handle(GetTasksByProjectAndAssigneeQuery query);
}
