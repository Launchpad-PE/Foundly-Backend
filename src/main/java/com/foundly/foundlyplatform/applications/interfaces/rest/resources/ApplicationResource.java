package com.foundly.foundlyplatform.applications.interfaces.rest.resources;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;

import java.util.Date;

/**
 * Read model returned by the Applications REST API.
 * Fields match the frontend Application entity schema.
 */
public record ApplicationResource(
        Long id,
        Long projectId,
        Long userId,
        String roleId,
        ApplicationStatus status,
        String fullName,
        String email,
        String portfolioUrl,
        String phone,
        String cvUrl,
        String message,
        boolean acceptedTerms,
        String avatarUrl,
        Date createdAt,
        Date updatedAt
) {}
