package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record CardInfoResource(
        String title,
        List<String> items
) {}