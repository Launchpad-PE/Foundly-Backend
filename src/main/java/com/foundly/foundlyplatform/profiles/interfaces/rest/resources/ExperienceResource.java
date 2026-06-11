package com.foundly.foundlyplatform.profiles.interfaces.rest.resources;

public record ExperienceResource(
        String id,
        String title,
        String company,
        String period,
        String description,
        Boolean current,
        String startDate,
        String endDate
) {}
