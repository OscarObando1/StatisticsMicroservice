package com.ozkin.microservice.firstMicroservice.entities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingYear {

    private Integer year;
    private Integer trainingSummaryDuration;
    private List<TrainingMonth> months = new ArrayList<>();
}
