package com.foundly.foundlyplatform.applications.interfaces.rest.resources;

import com.foundly.foundlyplatform.applications.domain.model.valueobjects.ApplicationStatus;

/**
 * Request body for accepting or rejecting an application.
 */
public record UpdateApplicationStatusResource(ApplicationStatus status) {}
