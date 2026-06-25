package com.foundly.foundlyplatform.messaging.application.commandservices;

import com.foundly.foundlyplatform.messaging.domain.model.aggregates.DirectMessage;
import com.foundly.foundlyplatform.messaging.domain.model.commands.SendDirectMessageCommand;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;

/**
 * Application service contract for direct message commands.
 */
public interface DirectMessageCommandService {

    /**
     * Handles sending a direct message.
     *
     * @param command send-message command
     * @return the persisted message, or an application error
     */
    Result<DirectMessage, ApplicationError> handle(SendDirectMessageCommand command);
}
