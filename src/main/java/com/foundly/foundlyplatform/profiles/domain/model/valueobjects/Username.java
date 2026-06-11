package com.foundly.foundlyplatform.profiles.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record Username (String username){

    public Username{
        if(username == null || username.isBlank())
            throw new IllegalArgumentException("Username no puede estar vacio");
        if(username.length() < 3)
            throw new IllegalArgumentException("Username debe tener al menos 3 caracteres");
        if (username.length() > 50)
            throw new IllegalArgumentException("Username no puede tener mas de 50 caracteres");

    }

    public static Username of(String value) {
        return new Username(value);
    }

}
