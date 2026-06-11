package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

import java.util.List;

public record PatchProfileResource(
        String username,
        String avatar,
        String bio,
        String role,
        List<String> skills,
        List<ExperienceResource> experiences,
        List<String> favoriteProjectIds,
        Boolean isComplete
) {}
