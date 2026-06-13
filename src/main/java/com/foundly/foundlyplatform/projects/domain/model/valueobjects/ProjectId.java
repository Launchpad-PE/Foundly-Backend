package com.foundly.foundlyplatform.projects.domain.model.valueobjects;

public record ProjectId(String value) {
    public ProjectId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Project ID no puede estar vacío o nulo");
        }
    }

    public static ProjectId of(String value) {
        return new ProjectId(value);
    }

    public static ProjectId generate() {
        return new ProjectId(java.util.UUID.randomUUID().toString());
    }
}