package com.foundly.foundlyplatform.profiles.interfaces.rest.transform;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.ExperienceResource;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile profile) {
        var experiences = profile.getExperiences().stream()
                .map(e -> new ExperienceResource(
                        e.getExperienceId(),
                        e.getTitle(),
                        e.getCompany(),
                        e.getPeriod(),
                        e.getDescription(),
                        e.getCurrent(),
                        e.getStartDate() != null ? e.getStartDate().toString() : null,
                        e.getEndDate()   != null ? e.getEndDate().toString()   : null
                ))
                .toList();

        return new ProfileResource(
                profile.getId(),
                profile.getUserId(),
                profile.getUsernameValue(),
                profile.getAvatar(),
                profile.getBioValue(),
                profile.getRoleValue(),
                profile.getSkills(),
                experiences,
                profile.getFavoriteProjectIds(),
                profile.isComplete()
        );
    }
}



