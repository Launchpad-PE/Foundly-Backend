package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record CreateMilestoneResource(
        @NotBlank String projectId,
        @NotBlank String creatorId,
        @NotBlank String title,
        String description,
        @NotNull Date dueDate,
        List<String> tools,
        String generalComment,
        List<String> attachments,
        List<CreateTaskResource> tasks
) {}
