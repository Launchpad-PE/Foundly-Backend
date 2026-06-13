package com.foundly.foundlyplatform.projects.domain.model.commands;

public record UpdateProjectCommand(
        String projectId,
        String name,
        String area,
        String summary,
        String academicLevel
) {}