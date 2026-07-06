package com.foundly.foundlyplatform.profiles.application.internal.queries;

import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import com.foundly.foundlyplatform.profiles.infrastructure.persistance.jpa.repositories.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional  // ✅ AGREGAR ESTO
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        Optional<Profile> profile = profileRepository.findByUserId(query.userId());

        // ✅ Forzar carga de las colecciones dentro de la transacción
        profile.ifPresent(p -> {
            p.getExperiences().size();  // Forzar carga de experiencias
            p.getSkills().size();       // Forzar carga de habilidades
            p.getFavoriteProjectIds().size(); // Forzar carga de favoritos
        });

        return profile;
    }

    @Override
    @Transactional  // ✅ AGREGAR ESTO
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();

        // ✅ Forzar carga de colecciones para cada perfil
        profiles.forEach(p -> {
            p.getExperiences().size();
            p.getSkills().size();
            p.getFavoriteProjectIds().size();
        });

        return profiles;
    }
}