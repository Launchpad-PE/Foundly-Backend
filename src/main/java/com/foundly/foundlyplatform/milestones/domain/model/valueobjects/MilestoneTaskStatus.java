package com.foundly.foundlyplatform.milestones.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MilestoneTaskStatus {
    PENDING,
    COMPLETED,
    DELAYED;

    @JsonValue
    public String getValue() {
        return this.name().toLowerCase();
    }
}