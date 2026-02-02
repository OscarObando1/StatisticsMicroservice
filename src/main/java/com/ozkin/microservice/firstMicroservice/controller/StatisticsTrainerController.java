package com.ozkin.microservice.firstMicroservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.services.TrainingStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatisticsTrainerController {

    private final TrainingStatisticsService trainingStatisticsService;

    @PostMapping("/statistics")
    public ResponseEntity<?> saveTrainingStatistics(@RequestBody TrainingRecordDto dto) {
        trainingStatisticsService.addTrainingRecord(dto);
        return ResponseEntity.ok().build();
    }


}
