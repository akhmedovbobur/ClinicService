package com.projects.clinicservice.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientFilterDto extends FilterDto{

    private String name;
    private String surname;
    private LocalDate startBirth;
    private LocalDate endBirth;
    private String contact;
    private Integer age;

}
