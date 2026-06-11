package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProfileResource(
        @NotNull String userId,
        @NotBlank String username,
        @NotBlank String bio,
        @NotBlank String role
) {}