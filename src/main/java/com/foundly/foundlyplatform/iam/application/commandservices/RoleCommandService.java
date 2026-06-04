package com.foundly.foundlyplatform.iam.application.commandservices;

import com.foundly.foundlyplatform.iam.domain.model.commands.SeedRolesCommand;

/**
 * Application service contract for IAM role commands.
 */
public interface RoleCommandService {
    /**
     * Handles the role seeding command.
     *
     * @param command role-seeding command
     */
    void handle(SeedRolesCommand command);
}