package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateProjectResource(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank String area,
        List<String> tags,
        @NotBlank @Size(min = 10, max = 500) String summary,
        List<String> environmentalMetrics,
        String academicLevel,
        List<String> benefits,
        List<String> requiredSkills,
        @NotNull Integer durationAmount,
        @NotNull String durationType,
        List<RoleResource> roles
) {}