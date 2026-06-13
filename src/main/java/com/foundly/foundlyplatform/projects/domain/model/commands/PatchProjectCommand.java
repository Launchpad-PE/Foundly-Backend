package com.foundly.foundlyplatform.projects.domain.model.commands;

import com.foundly.foundlyplatform.projects.domain.model.valueobjects.DurationType;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.EnvironmentalMetric;
import com.foundly.foundlyplatform.projects.domain.model.valueobjects.ProjectStatus;

import java.util.List;

public record PatchProjectCommand(
        String projectId,
        String name,
        String area,
        List<String> tags,
        String summary,
        List<EnvironmentalMetric> environmentalMetrics,
        String academicLevel,
        List<String> benefits,
        List<String> requiredSkills,
        Integer durationAmount,
        DurationType durationType,
        ProjectStatus status
) {}