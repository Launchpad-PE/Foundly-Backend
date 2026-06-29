package com.foundly.foundlyplatform.tasks.domain.model.commands;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;

import java.util.Date;
import java.util.List;

public record CreateTaskCommand(
        String projectId,
        String assigneeId,
        String creatorId,
        String title,
        String description,
        Date dueDate,
        List<Task.ChecklistItem> checklist,
        List<String> attachments,
        List<String> tools,
        String comment
) {}
