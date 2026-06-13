package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

public record MetricCardResource(
        String metric,
        String value,
        String unit,
        String status,
        String icon,
        String color
) {}