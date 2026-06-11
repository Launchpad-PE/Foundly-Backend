package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProfileResource(
        @NotNull String userId,
        @NotBlank String username,
        String avatar,
        @NotBlank String bio,
        @NotBlank String role,
        java.util.List<String> skills,
        java.util.List<ExperienceResource> experiences,
        Boolean isComplete
) {}