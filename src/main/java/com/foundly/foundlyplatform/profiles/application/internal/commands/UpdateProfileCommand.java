package com.foundly.foundlyplatform.profiles.application.internal.commands;

public record UpdateProfileCommand(
        Long profileId,
        String username,
        String avatar,
        String bio,
        String role
) {}