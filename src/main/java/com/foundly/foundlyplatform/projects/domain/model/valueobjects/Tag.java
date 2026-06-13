package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Tag(String value) {
    public Tag {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tag cannot be null or blank");
        }
        String cleaned = value.trim().replaceAll("^#", "").replaceAll("\\s+", " ");
        if (cleaned.length() < 2) {
            throw new IllegalArgumentException("Tag must have at least 2 characters");
        }
        if (cleaned.length() > 40) {
            throw new IllegalArgumentException("Tag cannot exceed 40 characters");
        }
        if (!cleaned.matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$")) {
            throw new IllegalArgumentException("Tag can only contain letters, numbers, spaces and hyphens");
        }
        value = cleaned;
    }

    public static Tag of(String value) {
        return new Tag(value);
    }

    public String getDisplayValue() {
        return "#" + value;
    }
}