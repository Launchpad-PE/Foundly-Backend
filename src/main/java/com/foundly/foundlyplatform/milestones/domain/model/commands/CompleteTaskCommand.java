package com.foundly.foundlyplatform.milestones.domain.model.commands;

public record CompleteTaskCommand(
        String taskId,
        String deliveryUrl,
        String deliveryNotes
) {}
