package com.foundly.foundlyplatform.milestones.domain.model.commands;

import java.util.Date;
import java.util.List;

public record UpdateMilestoneCommand(
        String milestoneId,
        String title,
        String description,
        Date dueDate,
        List<String> tools,
        String generalComment,
        List<String> attachments
) {}
