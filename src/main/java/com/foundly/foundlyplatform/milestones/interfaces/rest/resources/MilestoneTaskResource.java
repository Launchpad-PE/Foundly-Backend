package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;

import java.util.Date;
import java.util.List;

public record MilestoneTaskResource(
        String id,
        String milestoneId,
        String title,
        String description,
        String assigneeId,
        Date dueDate,
        List<ChecklistStepResource> checklist,
        List<String> attachments,
        MilestoneTaskStatus status,
        String deliveryUrl,
        String deliveryNotes,
        Date createdAt,
        Date updatedAt
) {}
