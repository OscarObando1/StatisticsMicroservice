package com.ozkin.microservice.firstMicroservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_year")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "training_summary_duration")
    private Integer trainingSummaryDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private StatisticsTrainer trainer;

    @OneToMany(mappedBy = "trainingYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingMonth> months = new ArrayList<>();
}
