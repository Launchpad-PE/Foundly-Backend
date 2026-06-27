package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import java.util.Date;
import java.util.List;

public record UpdateMilestoneResource(
        String title,
        String description,
        Date dueDate,
        List<String> tools,
        String generalComment,
        List<String> attachments
) {}
