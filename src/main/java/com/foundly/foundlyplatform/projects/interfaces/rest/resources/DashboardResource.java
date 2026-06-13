package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record DashboardResource(
        List<MetricCardResource> metrics,
        List<MetricTrendResource> trends,
        List<AlertResource> alerts
) {}