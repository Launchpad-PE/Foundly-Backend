package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CardTitle(String value) {
    public CardTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Card title cannot be null or blank");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Card title must have at least 3 characters");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("Card title cannot exceed 100 characters");
        }
    }

    public static CardTitle of(String value) {
        return new CardTitle(value);
    }
}