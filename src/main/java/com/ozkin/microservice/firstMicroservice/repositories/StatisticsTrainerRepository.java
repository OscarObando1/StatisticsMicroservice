package com.ozkin.microservice.firstMicroservice.repositories;

import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsTrainerRepository extends JpaRepository<StatisticsTrainer,Long>{

}
