package com.foundly.foundlyplatform.profiles.interfaces.rest.transform;
import com.foundly.foundlyplatform.profiles.application.internal.commands.CreateProfileCommand;
import com.foundly.foundlyplatform.profiles.interfaces.rest.resources.CreateProfileResource;

public class CreateProfileCommandFromAssembler {

    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        return new CreateProfileCommand(
                resource.userId(),
                resource.username(),
                resource.bio(),
                resource.role()
        );
    }

}



