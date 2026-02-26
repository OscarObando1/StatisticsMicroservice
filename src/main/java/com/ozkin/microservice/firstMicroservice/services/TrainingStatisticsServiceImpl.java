package com.ozkin.microservice.firstMicroservice.services;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.entities.TrainingMonth;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import com.ozkin.microservice.firstMicroservice.repositories.StatisticsTrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingStatisticsServiceImpl implements TrainingStatisticsService {

    private final StatisticsTrainerRepository trainerRepository;

    @Override
    public void addTrainingRecord(TrainingRecordDto dto) {
        Integer durationBox = dto.getTrainingDuration();
        int duration = durationBox != null ? durationBox : 0;
        var trainingDate = dto.getTrainingDate();
        if (trainingDate == null) return;

        StatisticsTrainer trainer = findOrCreateTrainer(dto);
        TrainingYear year = findOrCreateYear(trainer, trainingDate.getYear());
        TrainingMonth month = findOrCreateMonth(year, trainingDate.getMonthValue());

        Integer monthDuration = month.getDuration();
        int newDuration = (monthDuration != null ? monthDuration : 0) + duration;
        month.setDuration(newDuration);

        Integer yearSummaryBox = year.getTrainingSummaryDuration();
        int yearSummary = (yearSummaryBox != null ? yearSummaryBox : 0) + duration;
        year.setTrainingSummaryDuration(yearSummary);

        trainerRepository.save(trainer);
    }

    private StatisticsTrainer findOrCreateTrainer(TrainingRecordDto dto) {
        return trainerRepository.findByTrainerUsername(dto.getTrainerUsername())
                .orElseGet(() -> {
                    StatisticsTrainer t = new StatisticsTrainer();
                    t.setTrainerUsername(dto.getTrainerUsername());
                    t.setTrainerFirstName(dto.getTrainerFirstName());
                    t.setTrainerLastName(dto.getTrainerLastName());
                    Boolean isActive = dto.getIsActive();
                    t.setTrainerStatus(isActive == null || isActive);
                    return trainerRepository.save(t);
                });
    }

    private TrainingYear findOrCreateYear(StatisticsTrainer trainer, int yearValue) {
        if (trainer.getYears() == null) {
            trainer.setYears(new java.util.ArrayList<>());
        }
        Optional<TrainingYear> existing = trainer.getYears().stream()
                .filter(y -> yearValue == (y.getYear() != null ? y.getYear() : 0))
                .findFirst();
        if (existing.isPresent()) {
            return existing.get();
        }
        TrainingYear year = new TrainingYear();
        year.setYear(yearValue);
        year.setTrainingSummaryDuration(0);
        trainer.getYears().add(year);
        return year;
    }

    private TrainingMonth findOrCreateMonth(TrainingYear year, int monthNumber) {
        if (year.getMonths() == null) {
            year.setMonths(new java.util.ArrayList<>());
        }
        Optional<TrainingMonth> existing = year.getMonths().stream()
                .filter(m -> monthNumber == (m.getMonthNumber() != null ? m.getMonthNumber() : 0))
                .findFirst();
        if (existing.isPresent()) {
            return existing.get();
        }
        TrainingMonth month = new TrainingMonth();
        month.setMonthNumber(monthNumber);
        month.setDuration(0);
        year.getMonths().add(month);
        return month;
    }
}
