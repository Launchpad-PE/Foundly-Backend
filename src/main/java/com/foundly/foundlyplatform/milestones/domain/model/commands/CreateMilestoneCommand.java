package com.foundly.foundlyplatform.milestones.domain.model.commands;

import com.foundly.foundlyplatform.milestones.domain.model.entities.MilestoneTask;
import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.ChecklistStep;

import java.util.Date;
import java.util.List;

public record CreateMilestoneCommand(
        String projectId,
        String creatorId,
        String title,
        String description,
        Date dueDate,
        List<String> tools,
        String generalComment,
        List<String> attachments,
        List<CreateTaskInMilestoneCommand> tasks
) {}
