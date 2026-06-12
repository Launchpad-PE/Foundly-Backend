package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public record Area(String value) {
    public static final List<String> VALID_AREAS = List.of(
            "Tecnología", "Ciencia", "Arte", "Educación", "Social", "Ambiental"
    );

    public Area {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Area cannot be null or blank");
        }
        if (!VALID_AREAS.contains(value)) {
            throw new IllegalArgumentException("Area must be one of: " + VALID_AREAS);
        }
    }

    public static Area of(String value) {
        return new Area(value);
    }
}