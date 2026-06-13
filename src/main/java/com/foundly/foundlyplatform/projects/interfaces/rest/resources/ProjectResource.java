package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record ProjectResource(
        String id,
        String name,
        String area,
        List<String> tags,
        String summary,
        List<String> environmentalMetrics,
        String academicLevel,
        List<String> benefits,
        List<String> requiredSkills,
        DurationResource duration,
        List<RoleResource> roles,
        String status,
        Long authorId,
        String authorName,
        String createdAt,
        String updatedAt
) {}