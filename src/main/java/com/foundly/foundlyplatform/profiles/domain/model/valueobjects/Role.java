package com.foundly.foundlyplatform.profiles.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record Role (String role) {

    public Role {
        if( role == null || role.isBlank())
            throw new IllegalArgumentException("Rol no peude ser nulo");
        if (role.length() > 100)
            throw new IllegalArgumentException("Rol no puede tener mas de 100 caracteres ");

    }

    public static Role of(String value){
        return new Role(value);
    }
}
