package com.ozkin.microservice.firstMicroservice.repositories;

import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StatisticsTrainerRepository extends MongoRepository<StatisticsTrainer, String> {

    Optional<StatisticsTrainer> findByTrainerUsername(String trainerUsername);
}
