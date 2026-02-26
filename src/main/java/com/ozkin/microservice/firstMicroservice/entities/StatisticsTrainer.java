package com.ozkin.microservice.firstMicroservice.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "statistics_trainer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTrainer {

    @Id
    private String id;

    @Indexed(unique = true)
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private Boolean trainerStatus;
    private List<TrainingYear> years = new ArrayList<>();
}
