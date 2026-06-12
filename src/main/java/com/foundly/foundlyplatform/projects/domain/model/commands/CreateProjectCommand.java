package com.foundly.foundlyplatform.projects.domain.model.commands;

import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.DurationType;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.EnvironmentalMetric;

import java.util.List;

public record CreateProjectCommand(
        String name,
        String area,
        List<String> tags,
        String summary,
        List<EnvironmentalMetric> environmentalMetrics,
        String academicLevel,
        List<String> benefits,
        List<String> requiredSkills,
        int durationAmount,
        DurationType durationType,
        List<ProjectRole> roles,
        Long authorId,
        String authorName
) {}