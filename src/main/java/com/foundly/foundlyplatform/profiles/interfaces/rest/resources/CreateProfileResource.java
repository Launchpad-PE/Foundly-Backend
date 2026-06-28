package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProfileResource(
        @NotNull(message = "{validation.not-null}") String userId,
        @NotBlank(message = "{validation.not-blank}") String username,
        String avatar,
        @NotBlank(message = "{validation.not-blank}") String bio,
        @NotBlank(message = "{validation.not-blank}") String role,
        java.util.List<String> skills,
        java.util.List<ExperienceResource> experiences,
        Boolean isComplete
) {}