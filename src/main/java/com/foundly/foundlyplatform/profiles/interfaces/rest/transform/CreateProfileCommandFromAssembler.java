package com.foundly.foundlyplatform.profiles.interfaces.rest.transform;

import com.foundly.foundlyplatform.profiles.application.internal.commands.CreateProfileCommand;
import com.foundly.foundlyplatform.profiles.domain.model.entities.Experience;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.CreateProfileResource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class CreateProfileCommandFromAssembler {

    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        List<Experience> experiences = resource.experiences() == null ? Collections.emptyList() :
                resource.experiences().stream()
                        .map(e -> new Experience(
                                e.id(), e.title(), e.company(), e.period(), e.description(), e.current(),
                                e.startDate() != null ? LocalDate.parse(e.startDate()) : null,
                                e.endDate()   != null ? LocalDate.parse(e.endDate())   : null
                        ))
                        .toList();

        return new CreateProfileCommand(
                Long.parseLong(resource.userId()),
                resource.username(),
                resource.avatar(),
                resource.bio(),
                resource.role(),
                resource.skills() != null ? resource.skills() : Collections.emptyList(),
                experiences,
                Boolean.TRUE.equals(resource.isComplete())
        );
    }
}



