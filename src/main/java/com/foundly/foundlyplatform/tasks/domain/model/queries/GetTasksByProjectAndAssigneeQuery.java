package com.foundly.foundlyplatform.tasks.domain.model.queries;

public record GetTasksByProjectAndAssigneeQuery(String projectId, String assigneeId) {}
