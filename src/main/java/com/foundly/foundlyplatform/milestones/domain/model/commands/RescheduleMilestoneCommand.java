package com.foundly.foundlyplatform.milestones.domain.model.commands;

import java.util.Date;

public record RescheduleMilestoneCommand(String milestoneId, Date newDueDate) {}
