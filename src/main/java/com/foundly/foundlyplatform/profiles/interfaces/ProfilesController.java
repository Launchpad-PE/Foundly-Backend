package com.foundly.foundlyplatform.profiles.interfaces;


import com.foundly.foundlyplatform.profiles.application.internal.commands.PatchProfileCommand;
import com.foundly.foundlyplatform.profiles.application.internal.commands.ProfileCommandService;
import com.foundly.foundlyplatform.profiles.application.internal.commands.UpdateProfileCommand;
import com.foundly.foundlyplatform.profiles.application.internal.queries.GetProfileByIdQuery;
import com.foundly.foundlyplatform.profiles.application.internal.queries.GetProfileByUserIdQuery;
import com.foundly.foundlyplatform.profiles.application.internal.queries.ProfileQueryService;
import com.foundly.foundlyplatform.profiles.domain.model.entities.Experience;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.CreateProfileResource;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.PatchProfileResource;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.ProfileResource;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.UpdateProfileResource;
import com.foundly.foundlyplatform.profiles.interfaces.rest.transform.CreateProfileCommandFromAssembler;
import com.foundly.foundlyplatform.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "Profiles Management Endpoints")
public class ProfilesController {

    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService,
                              ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    // POST /api/v1/profiles
    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@Valid @RequestBody CreateProfileResource resource) {
        var command = CreateProfileCommandFromAssembler.toCommandFromResource(resource);
        return profileCommandService.handle(command)
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> ResponseEntity.status(HttpStatus.CREATED).body(r))
                .orElse(ResponseEntity.badRequest().build());
    }

    // GET /api/v1/profiles
    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {
        var profiles = profileQueryService.getAllProfiles()
                .stream()
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(profiles);
    }

    // GET /api/v1/profiles/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable Long id) {
        return profileQueryService.handle(new GetProfileByIdQuery(id))
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/v1/profiles/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProfileResource> getProfileByUserId(@PathVariable Long userId) {
        return profileQueryService.handle(new GetProfileByUserIdQuery(userId))
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/profiles/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResource> updateProfile(@PathVariable Long id,
                                                         @RequestBody UpdateProfileResource resource) {
        var command = new UpdateProfileCommand(id, resource.username(), resource.avatar(),
                resource.bio(), resource.role());
        return profileCommandService.handle(command)
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/v1/profiles/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ProfileResource> patchProfile(@PathVariable Long id,
                                                        @RequestBody PatchProfileResource resource) {
        List<Experience> experiences = null;
        if (resource.experiences() != null) {
            experiences = resource.experiences().stream()
                    .map(e -> new Experience(
                            e.id(), e.title(), e.company(), e.period(), e.description(), e.current(),
                            e.startDate() != null ? LocalDate.parse(e.startDate()) : null,
                            e.endDate()   != null ? LocalDate.parse(e.endDate())   : null
                    ))
                    .toList();
        }
        var command = new PatchProfileCommand(id, resource.username(), resource.avatar(),
                resource.bio(), resource.role(), resource.skills(), experiences,
                resource.favoriteProjectIds(), resource.isComplete());
        return profileCommandService.handle(command)
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/profiles/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        profileCommandService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

}

