package com.foundly.foundlyplatform.profiles.application.internal.commands;

import com.foundly.foundlyplatform.profiles.domain.model.entities.Experience;

import java.util.List;

public record PatchProfileCommand(
        Long profileId,
        String username,
        String avatar,
        String bio,
        String role,
        List<String> skills,
        List<Experience> experiences,
        List<String> favoriteProjectIds,
        Boolean isComplete
) {}
