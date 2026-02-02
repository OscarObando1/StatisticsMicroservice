package com.ozkin.microservice.firstMicroservice.services;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;

public interface TrainingStatisticsService {

    void addTrainingRecord(TrainingRecordDto dto);
}
