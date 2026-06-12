package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public record AcademicLevel(String value) {
    public static final List<String> VALID_LEVELS = List.of(
            "Pregrado", "Postgrado", "Doctorado", "Técnico", "Autodidacta"
    );

    public AcademicLevel {
        if (value != null && !VALID_LEVELS.contains(value)) {
            throw new IllegalArgumentException("Academic level must be one of: " + VALID_LEVELS);
        }
    }

    public static AcademicLevel of(String value) {
        if (value == null) return null;
        return new AcademicLevel(value);
    }
}