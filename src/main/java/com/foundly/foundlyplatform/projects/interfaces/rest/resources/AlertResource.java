package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

public record AlertResource(
        String level,
        String message,
        String time
) {}