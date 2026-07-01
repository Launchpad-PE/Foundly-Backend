package com.foundly.foundlyplatform.milestones.interfaces.rest.resources;

import com.foundly.foundlyplatform.milestones.domain.model.valueobjects.MilestoneTaskStatus;

public record UpdateTaskStatusResource(MilestoneTaskStatus status) {}
