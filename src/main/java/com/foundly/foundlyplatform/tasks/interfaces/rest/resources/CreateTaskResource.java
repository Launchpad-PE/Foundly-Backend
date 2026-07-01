package com.foundly.foundlyplatform.tasks.interfaces.rest.resources;

import java.util.Date;
import java.util.List;

public record CreateTaskResource(
        String projectId,
        String assigneeId,
        String creatorId,
        String title,
        String description,
        Date dueDate,
        List<ChecklistItemResource> checklist,
        List<String> attachments,
        List<String> tools,
        String comment
) {}
