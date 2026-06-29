package com.foundly.foundlyplatform.applications.interfaces.rest.transform;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.ApplicationResource;

/**
 * Assembler that converts an Application domain aggregate to an ApplicationResource.
 */
public final class ApplicationResourceFromEntityAssembler {

    private ApplicationResourceFromEntityAssembler() {}

    public static ApplicationResource toResourceFromEntity(Application application) {
        return new ApplicationResource(
                application.getId(),
                application.getProjectId(),
                application.getUserId(),
                application.getRoleId(),
                application.getStatus(),
                application.getFullName(),
                application.getEmail(),
                application.getPortfolioUrl(),
                application.getPhone(),
                application.getCvUrl(),
                application.getMessage(),
                application.isAcceptedTerms(),
                application.getAvatarUrl(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
