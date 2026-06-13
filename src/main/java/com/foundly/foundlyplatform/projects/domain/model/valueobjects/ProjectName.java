package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ProjectName(String value) {
    public ProjectName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Project name must have at least 3 characters");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Project name cannot exceed 100 characters");
        }
    }

    public static ProjectName of(String value) {
        return new ProjectName(value);
    }
}