package com.foundly.foundlyplatform.applications.domain.model.queries;

/**
 * Query to retrieve applications filtered by both project and user.
 *
 * <p>Useful to check whether a specific user has already applied to a project,
 * or to load the application of an accepted collaborator.</p>
 *
 * @param projectId identifier of the project
 * @param userId    identifier of the applicant
 */
public record GetApplicationsByProjectAndUserIdQuery(Long projectId, Long userId) {}
