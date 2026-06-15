package com.foundly.foundlyplatform.applications.domain.model.commands;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;

/**
 * Command to update the status of an existing application.
 *
 * @param applicationId identifier of the application to update
 * @param status        new status (ACCEPTED or REJECTED)
 */
public record UpdateApplicationStatusCommand(Long applicationId, ApplicationStatus status) {}
