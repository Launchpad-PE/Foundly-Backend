package com.foundly.foundlyplatform.profiles.application.internal.commands;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;

import java.util.Optional;

public interface ProfileCommandService {

    Optional<Profile> handle(CreateProfileCommand command);

    Optional<Profile> handle(UpdateProfileCommand command);

    void deleteProfile(Long profileId);
}