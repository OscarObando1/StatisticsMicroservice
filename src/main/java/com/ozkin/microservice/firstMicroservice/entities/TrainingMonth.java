package com.ozkin.microservice.firstMicroservice.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "training_month")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "month_number", nullable = false)
    private Integer monthNumber;

    @Column(name = "duration")
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_year_id", nullable = false)
    private TrainingYear trainingYear;
}
