package com.foundly.foundlyplatform.applications.domain.model.queries;

/**
 * Query to retrieve all applications submitted by a given user (collaborator).
 *
 * @param userId identifier of the applicant
 */
public record GetApplicationsByUserIdQuery(String userId) {}
