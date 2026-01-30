package com.ozkin.microservice.firstMicroservice.utils;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.entities.TrainingMonth;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public StatisticsTrainer statisticsTrainermapper(TrainingRecordDto dto){
        StatisticsTrainer entity = new StatisticsTrainer();
        entity.setTrainerUsername(dto.getTrainerUsername());
        entity.setTrainerFirstName(dto.getTrainerFirstName());
        entity.setTrainerLastName(dto.getTrainerLastName());
        entity.setTrainerStatus(dto.getIsActive());

        TrainingYear year = new TrainingYear();
        year.setYear(dto.getTrainingDate().getYear());
        year.setTrainer(entity);

        TrainingMonth month = new TrainingMonth();
        month.setMonthNumber(dto.getTrainingDate().getMonthValue());
        month.setDuration(dto.getTrainingDuration());
        month.setTrainingYear(year);

        year.getMonths().add(month);
        entity.getYears().add(year);

        return entity;
    }


}
