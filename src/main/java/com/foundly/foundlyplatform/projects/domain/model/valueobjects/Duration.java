package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Duration(int amount, DurationType type) {
    public Duration {
        if (amount <= 0) {
            throw new IllegalArgumentException("Duration amount must be positive");
        }
    }

    public static Duration of(int amount, DurationType type) {
        return new Duration(amount, type);
    }

    public String toString() {
        return amount + " " + type.getDisplayName();
    }
}