package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record PatchProjectResource(
        String name,
        String area,
        List<String> tags,
        String summary,
        List<String> environmentalMetrics,
        String academicLevel,
        List<String> benefits,
        List<String> requiredSkills,
        Integer durationAmount,
        String durationType,
        String status
) {}