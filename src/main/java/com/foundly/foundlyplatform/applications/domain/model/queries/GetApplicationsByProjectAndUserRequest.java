package com.foundly.foundlyplatform.applications.domain.model.queries;

public record GetApplicationsByProjectAndUserRequest(
        String projectId,
        String userId
) {}