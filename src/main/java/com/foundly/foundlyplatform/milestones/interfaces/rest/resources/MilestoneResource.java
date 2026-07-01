package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneStatus;

import java.util.Date;
import java.util.List;

public record MilestoneResource(
        String id,
        String projectId,
        String creatorId,
        String title,
        String description,
        List<String> tools,
        String generalComment,
        Date dueDate,
        List<String> attachments,
        List<MilestoneTaskResource> tasks,
        MilestoneStatus status,
        String deliveryUrl,
        String deliveryNotes,
        int tasksCompletionPercentage,
        Date createdAt,
        Date updatedAt
) {}
