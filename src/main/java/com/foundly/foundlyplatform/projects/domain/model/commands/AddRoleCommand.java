package com.foundly.foundlyplatform.projects.domain.model.commands;

import com.foundly.foundlyplatform.projects.domain.model.entities.ProjectRole;

public record AddRoleCommand(String projectId, ProjectRole role) {}

