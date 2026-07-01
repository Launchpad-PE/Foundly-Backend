package com.foundly.foundlyplatform.tasks.interfaces.rest.resources;

import com.foundly.foundlyplatform.tasks.domain.model.valueobjects.TaskStatus;

import java.util.Date;
import java.util.List;

public record PatchTaskResource(
        String title,
        String description,
        Date dueDate,
        List<ChecklistItemResource> checklist,
        List<String> attachments,
        List<String> tools,
        String comment,
        TaskStatus status,
        String deliveryUrl,
        String deliveryNotes
) {}
