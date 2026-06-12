package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Skill(String name) {
    public Skill {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill cannot be null or blank");
        }
        String cleaned = name.trim().replaceAll("^#", "");
        if (cleaned.length() < 2) {
            throw new IllegalArgumentException("Skill must have at least 2 characters");
        }
        if (cleaned.length() > 30) {
            throw new IllegalArgumentException("Skill cannot exceed 30 characters");
        }
        name = cleaned;
    }

    public static Skill of(String name) {
        return new Skill(name);
    }

    public String getDisplayValue() {
        return "#" + name;
    }
}