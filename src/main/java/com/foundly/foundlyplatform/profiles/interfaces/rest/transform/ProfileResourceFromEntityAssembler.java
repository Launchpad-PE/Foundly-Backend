package com.foundly.foundlyplatform.profiles.interfaces.rest.transform;
import com.foundly.foundlyplatform.profiles.domain.model.aggregates.Profile;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.ProfileResource;


public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(Profile profile) {
        return new ProfileResource(
                profile.getId(),
                profile.getUserId(),
                profile.getUsernameValue(),
                profile.getAvatar(),
                profile.getBioValue(),
                profile.getRoleValue(),
                profile.getSkills(),
                profile.getFavoriteProjectIds(),
                profile.isComplete()
        );
    }

}



