package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

public record UpdateProfileResource(
        String username,
        String avatar,
        String bio,
        String role
) {}