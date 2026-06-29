package com.foundly.foundlyplatform.applications.domain.model.commands;

/**
 * Command to submit an application to a project role.
 *
 * @param projectId     identifier of the target project
 * @param userId        identifier of the applicant (collaborator)
 * @param roleId        identifier of the role being applied to
 * @param fullName      full name of the applicant
 * @param email         contact email
 * @param portfolioUrl  optional portfolio / LinkedIn URL
 * @param phone         optional phone number
 * @param cvUrl         CV / resume URL (required)
 * @param message       presentation message (required)
 * @param acceptedTerms whether the applicant accepted the terms
 * @param avatarUrl     optional profile picture URL
 */
public record ApplyToProjectCommand(
        Long projectId,
        Long userId,
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
