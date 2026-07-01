package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import java.util.List;

public record CreateTaskResource(
        String title,
        String description,
        String assigneeId,
        List<ChecklistStepResource> checklist,
        List<String> attachments
) {}
