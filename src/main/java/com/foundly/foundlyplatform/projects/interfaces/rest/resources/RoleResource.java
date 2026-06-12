package com.foundly.foundlyplatform.projects.interfaces.rest.resources;

import java.util.List;

public record RoleResource(
        String name,
        CardInfoResource cardInfo
) {}