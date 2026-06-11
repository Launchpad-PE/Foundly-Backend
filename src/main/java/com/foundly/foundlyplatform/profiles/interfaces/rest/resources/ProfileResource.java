package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

import java.util.List;

public record ProfileResource(
        Long id,
        Long userId,
        String username,
        String avatar,
        String bio,
        String role,
        List<String> skills,
        List<String> favoriteProjectIds,
        boolean isComplete
) {}