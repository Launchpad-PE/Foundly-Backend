package com.foundly.foundlyplatform.tasks.domain.model.commands;

import com.foundly.foundlyplatform.tasks.domain.model.aggregates.Task;
import com.foundly.foundlyplatform.tasks.domain.model.valueobjects.TaskStatus;

import java.util.Date;
import java.util.List;

public record PatchTaskCommand(
        Long id,
        String title,
        String description,
        Date dueDate,
        List<Task.ChecklistItem> checklist,
        List<String> attachments,
        List<String> tools,
        String comment,
        TaskStatus status,
        String deliveryUrl,
        String deliveryNotes
) {}
