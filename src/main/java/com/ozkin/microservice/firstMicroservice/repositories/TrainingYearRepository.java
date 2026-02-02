package com.ozkin.microservice.firstMicroservice.repositories;

import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingYearRepository extends JpaRepository<TrainingYear, Long> {

    Optional<TrainingYear> findByTrainerAndYear(StatisticsTrainer trainer, Integer year);
}
