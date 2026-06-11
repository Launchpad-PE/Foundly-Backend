package com.foundly.foundlyplatform.profiles.application.internal.commands;

public record CreateProfileCommand(
        Long userId,
        String username,
        String bio,
        String role
) {}