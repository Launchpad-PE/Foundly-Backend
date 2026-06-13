package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Summary(String value) {
    public Summary {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Summary cannot be null or blank");
        }
        if (value.length() < 10) {
            throw new IllegalArgumentException("Summary must have at least 10 characters");
        }
        if (value.length() > 500) {
            throw new IllegalArgumentException("Summary cannot exceed 500 characters");
        }
    }

    public static Summary of(String value) {
        return new Summary(value);
    }
}