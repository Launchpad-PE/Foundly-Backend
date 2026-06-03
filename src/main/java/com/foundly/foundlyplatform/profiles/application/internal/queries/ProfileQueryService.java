package com.foundly.foundlyplatform.profiles.application.internal.queries;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;

import java.util.List;
import java.util.Optional;

public interface ProfileQueryService {

    Optional<Profile> handle(GetProfileByIdQuery query);

    Optional<Profile> handle(GetProfileByUserIdQuery query);

    List<Profile> getAllProfiles();
}