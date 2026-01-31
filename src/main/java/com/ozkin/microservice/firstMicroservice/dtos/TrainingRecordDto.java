package com.ozkin.microservice.firstMicroservice.dtos;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingRecordDto {


    private String trainerUsername;


    private String trainerFirstName;


    private String trainerLastName;


    private Boolean isActive;


    private LocalDate trainingDate;


    private Integer trainingDuration;


    private String actionType;
}
