package com.foundly.foundlyplatform.projects.domain.model.valueobjects;


import jakarta.persistence.Embeddable;

@Embeddable
public record CardItem(String description) {
    public CardItem {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Card item cannot be null or blank");
        }
        if (description.length() < 2) {
            throw new IllegalArgumentException("Card item must have at least 2 characters");
        }
        if (description.length() > 200) {
            throw new IllegalArgumentException("Card item cannot exceed 200 characters");
        }
    }

    public static CardItem of(String description) {
        return new CardItem(description);
    }
}
