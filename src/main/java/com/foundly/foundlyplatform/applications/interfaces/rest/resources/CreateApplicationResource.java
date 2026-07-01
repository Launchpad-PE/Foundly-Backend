package com.foundly.foundlyplatform.applications.interfaces.rest.resources;

/**
 * Request body for submitting a new application to a project role.
 * Matches the frontend's ApplicationResource schema.
 */
public record CreateApplicationResource(
        String  projectId,
        String  userId,
        String roleId,
        String fullName,
        String email,
        String portfolioUrl,
        String phone,
        String cvUrl,
        String message,
        boolean acceptedTerms,
        String avatarUrl
) {}
