package com.foundly.foundlyplatform.applications.domain.model.queries;

/**
 * Query to retrieve a single application by its identifier.
 *
 * @param applicationId identifier of the application
 */
public record GetApplicationByIdQuery(String applicationId) {}
