package com.ozkin.microservice.firstMicroservice.repositories;

import com.ozkin.microservice.firstMicroservice.entities.TrainingMonth;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingMonthRepository extends JpaRepository<TrainingMonth, Long> {

    Optional<TrainingMonth> findByTrainingYearAndMonthNumber(TrainingYear trainingYear, Integer monthNumber);
}
