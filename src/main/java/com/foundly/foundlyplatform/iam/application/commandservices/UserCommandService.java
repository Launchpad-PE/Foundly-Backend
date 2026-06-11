package com.foundly.foundlyplatform.iam.application.commandservices;

import com.foundly.foundlyplatform.iam.domain.model.aggregates.User;
import com.foundly.foundlyplatform.iam.domain.model.commands.SignInCommand;
import com.foundly.foundlyplatform.iam.domain.model.commands.SignUpCommand;
import com.foundly.foundlyplatform.shared.application.result.ApplicationError;
import com.foundly.foundlyplatform.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Application service contract for IAM user commands.
 */
public interface UserCommandService {
    /**
     * Handles user sign-in.
     *
     * @param command sign-in command
     * @return authenticated user and token pair, or an application error
     */
    Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);

    /**
     * Handles user sign-up.
     *
     * @param command sign-up command
     * @return created user aggregate, or an application error
     */
    Result<User, ApplicationError> handle(SignUpCommand command);
}
