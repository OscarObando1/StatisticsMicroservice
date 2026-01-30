package com.ozkin.microservice.firstMicroservice.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "statistics_trainer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsTrainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trainer_username", nullable = false, unique = true)
    private String trainerUsername;

    @Column(name = "trainer_first_name", nullable = false)
    private String trainerFirstName;

    @Column(name = "trainer_last_name", nullable = false)
    private String trainerLastName;

    @Column(name = "trainer_status", nullable = false)
    private Boolean trainerStatus;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingYear> years = new ArrayList<>();
}
