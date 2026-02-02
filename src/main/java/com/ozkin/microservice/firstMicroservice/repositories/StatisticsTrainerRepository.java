package com.ozkin.microservice.firstMicroservice.repositories;

import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsTrainerRepository extends JpaRepository<StatisticsTrainer, Long> {

    Optional<StatisticsTrainer> findByTrainerUsername(String trainerUsername);
}
