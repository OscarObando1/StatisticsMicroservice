package com.ozkin.microservice.firstMicroservice.controller;


import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.repositories.StatisticsTrainerRepository;
import com.ozkin.microservice.firstMicroservice.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerTest {

    @Autowired
    Mapper mapper;

    @Autowired
    StatisticsTrainerRepository repository;

    @PostMapping("/save")
    public String saveEntity(@RequestBody TrainingRecordDto dto){
        StatisticsTrainer trainer = mapper.statisticsTrainermapper(dto);
        repository.save(trainer);
        return "aja revisar db";
    }

}
