package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record RoleName(String value) {
    public RoleName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Role name must have at least 3 characters");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("Role name cannot exceed 50 characters");
        }
    }

    public static RoleName of(String value) {
        return new RoleName(value);
    }
}