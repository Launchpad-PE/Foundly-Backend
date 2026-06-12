package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record MetricTrendResource(
        String metric,
        List<Integer> values,
        List<String> days
) {}