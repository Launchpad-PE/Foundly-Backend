package com.foundly.foundlyplatform.milestones.domain.model.commands;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;

import java.util.List;

public record CreateTaskInMilestoneCommand(
        String title,
        String description,
        String assigneeId,
        List<ChecklistStep> checklist,
        List<String> attachments
) {}
