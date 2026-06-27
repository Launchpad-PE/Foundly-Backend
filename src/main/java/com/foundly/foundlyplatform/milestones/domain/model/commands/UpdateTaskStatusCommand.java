package com.foundly.foundlyplatform.milestones.domain.model.commands;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;

public record UpdateTaskStatusCommand(String taskId, MilestoneTaskStatus status) {}
