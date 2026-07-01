package com.foundly.foundlyplatform.applications.domain.model.queries;

/**
 * Query to retrieve all applications submitted for a given project.
 *
 * @param projectId identifier of the project
 */
public record GetApplicationsByProjectIdQuery(String projectId) {}
