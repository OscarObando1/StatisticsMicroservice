package com.ozkin.microservice.firstMicroservice.services;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.entities.TrainingMonth;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import com.ozkin.microservice.firstMicroservice.repositories.StatisticsTrainerRepository;
import com.ozkin.microservice.firstMicroservice.repositories.TrainingMonthRepository;
import com.ozkin.microservice.firstMicroservice.repositories.TrainingYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingStatisticsServiceImpl implements TrainingStatisticsService {

    private final StatisticsTrainerRepository trainerRepository;
    private final TrainingYearRepository yearRepository;
    private final TrainingMonthRepository monthRepository;

    @Override
    @Transactional
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

        monthRepository.save(month);
        yearRepository.save(year);
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
        Optional<TrainingYear> existing = yearRepository.findByTrainerAndYear(trainer, yearValue);
        if (existing.isPresent()) {
            return existing.get();
        }
        TrainingYear year = new TrainingYear();
        year.setYear(yearValue);
        year.setTrainer(trainer);
        year.setTrainingSummaryDuration(0);
        year = yearRepository.save(year);
        trainer.getYears().add(year);
        return year;
    }

    private TrainingMonth findOrCreateMonth(TrainingYear year, int monthNumber) {
        Optional<TrainingMonth> existing = monthRepository.findByTrainingYearAndMonthNumber(year, monthNumber);
        if (existing.isPresent()) {
            return existing.get();
        }
        TrainingMonth month = new TrainingMonth();
        month.setMonthNumber(monthNumber);
        month.setDuration(0);
        month.setTrainingYear(year);
        month = monthRepository.save(month);
        year.getMonths().add(month);
        return month;
    }
}
