package com.foundly.foundlyplatform.profiles.application.internal.commands;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Bio;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Role;
import com.foundly.foundlyplatform.profiles.domain.model.valueobjects.Username;
import com.foundly.foundlyplatform.profiles.infrastructure.persistance.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(CreateProfileCommand command) {
        if (profileRepository.existsByUserId(command.userId()))
            throw new IllegalArgumentException("Ya existe un perfil para este usuario");

        if (profileRepository.existsByUsernameUsername(command.username()))
            throw new IllegalArgumentException("El username ya esta en uso");

        var profile = new Profile(
                command.userId(),
                Username.of(command.username()),
                Bio.of(command.bio()),
                Role.of(command.role())
        );

        return Optional.of(profileRepository.save(profile));
    }

    @Override
    public Optional<Profile> handle(UpdateProfileCommand command) {
        return profileRepository.findById(command.profileId()).map(profile -> {
            if (command.username() != null) profile.updateUsername(command.username());
            if (command.avatar() != null)   profile.updateAvatar(command.avatar());
            if (command.bio() != null)      profile.updateBio(command.bio());
            if (command.role() != null)     profile.updateRole(command.role());
            return profileRepository.save(profile);
        });
    }

    @Override
    public void deleteProfile(Long profileId) {
        if (!profileRepository.existsById(profileId))
            throw new IllegalArgumentException("Perfil no encontrado con id: " + profileId);
        profileRepository.deleteById(profileId);
    }
}