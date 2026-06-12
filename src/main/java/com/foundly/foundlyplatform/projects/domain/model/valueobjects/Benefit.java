package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Benefit(String description) {
    public Benefit {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Benefit description cannot be null or blank");
        }
        if (description.length() < 5) {
            throw new IllegalArgumentException("Benefit must have at least 5 characters");
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException("Benefit cannot exceed 200 characters");
        }
    }

    public static Benefit of(String description) {
        return new Benefit(description);
    }
}