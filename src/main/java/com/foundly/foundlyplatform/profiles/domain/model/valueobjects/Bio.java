package com.foundly.foundlyplatform.profiles.domain.model.valueobjects;
import jakarta.persistence.Embeddable;

@Embeddable
public record  Bio(String bio) {

    public Bio{
        if(bio == null )
            throw new IllegalArgumentException("Biografia no puede ser vacio");
        if(bio.length() > 500)
            throw new IllegalArgumentException("Biografia no puede superar los 500 caracteres");


    }

    public static Bio of(String value) {
        return new Bio(value);
    }

}
