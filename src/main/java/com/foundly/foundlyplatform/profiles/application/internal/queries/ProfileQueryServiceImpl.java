package com.foundly.foundlyplatform.profiles.application.internal.queries;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import com.foundly.foundlyplatform.profiles.infrastructure.persistance.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }

    @Override
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
}