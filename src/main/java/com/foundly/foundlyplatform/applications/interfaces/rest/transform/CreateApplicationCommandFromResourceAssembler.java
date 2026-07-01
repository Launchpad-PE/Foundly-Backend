package com.foundly.foundlyplatform.applications.interfaces.rest.transform;

import com.foundly.foundlyplatform.applications.domain.model.commands.ApplyToProjectCommand;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.CreateApplicationResource;

/**
 * Assembler that converts a CreateApplicationResource into an ApplyToProjectCommand.
 */
public final class CreateApplicationCommandFromResourceAssembler {

    private CreateApplicationCommandFromResourceAssembler() {}

    public static ApplyToProjectCommand toCommandFromResource(CreateApplicationResource resource) {
        return new ApplyToProjectCommand(
                resource.projectId(),
                resource.userId(),
                resource.roleId(),
                resource.fullName(),
                resource.email(),
                resource.portfolioUrl(),
                resource.phone(),
                resource.cvUrl(),
                resource.message(),
                resource.acceptedTerms(),
                resource.avatarUrl()
        );
    }
}
