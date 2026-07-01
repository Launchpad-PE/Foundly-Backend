package com.foundly.foundlyplatform.applications.application.commandservices;

import com.foundly.foundlyplatform.applications.domain.model.aggregates.Application;
import com.foundly.foundlyplatform.applications.domain.model.commands.ApplyToProjectCommand;
import com.foundly.foundlyplatform.applications.domain.model.commands.UpdateApplicationStatusCommand;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;

/**
 * Application service contract for application commands.
 */
public interface ApplicationCommandService {

    /**
     * Handles a collaborator's application to a project role.
     *
     * @param command apply-to-project command
     * @return the created application, or an error
     */
    Result<Application, ApplicationError> handle(ApplyToProjectCommand command);

    /**
     * Handles an update to an existing application's status (accept / reject).
     *
     * @param command update-status command
     * @return the updated application, or an error
     */
    Result<Application, ApplicationError> handle(UpdateApplicationStatusCommand command);
}
