package com.foundly.foundlyplatform.profiles.domain.model.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Experience {


    private String experienceId;
    private String title;
    private String company;
    private String period;
    private String description;
    private Boolean current;
    private LocalDate startDate;
    private LocalDate endDate;

    public Experience(String experienceId,  String title,  String company, String period, String description,
                      Boolean current, LocalDate startDate, LocalDate endDate ){

        if(title == null || title.isBlank())
            throw new IllegalArgumentException("El titulo de la experiencia es requerida");
        if(company == null || company.isBlank())
            throw new IllegalArgumentException("La empresa es requerida");


        this.experienceId = experienceId;
        this.title = title;
        this.company = company;
        this.period = period;
        this.description = description;
        this.current = current;
        this.startDate = startDate;
        this.endDate = endDate;

    }


}
