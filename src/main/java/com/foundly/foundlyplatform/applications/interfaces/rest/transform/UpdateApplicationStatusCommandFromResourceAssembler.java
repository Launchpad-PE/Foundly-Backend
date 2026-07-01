package com.foundly.foundlyplatform.applications.interfaces.rest.transform;

import com.foundly.foundlyplatform.applications.domain.model.commands.UpdateApplicationStatusCommand;
import com.foundly.foundlyplatform.applications.interfaces.rest.resources.UpdateApplicationStatusResource;

/**
 * Assembler that converts an UpdateApplicationStatusResource into an UpdateApplicationStatusCommand.
 */
public final class UpdateApplicationStatusCommandFromResourceAssembler {

    private UpdateApplicationStatusCommandFromResourceAssembler() {}

    // ANTES: recibía Long
    // public static UpdateApplicationStatusCommand toCommandFromResource(Long applicationId, UpdateApplicationStatusResource resource)

    // AHORA: recibe String
    public static UpdateApplicationStatusCommand toCommandFromResource(
            String applicationId,  // ← Long → String
            UpdateApplicationStatusResource resource
    ) {
        return new UpdateApplicationStatusCommand(applicationId, resource.status());
    }
}