package com.ozkin.microservice.firstMicroservice.services;

import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.entities.StatisticsTrainer;
import com.ozkin.microservice.firstMicroservice.entities.TrainingMonth;
import com.ozkin.microservice.firstMicroservice.entities.TrainingYear;
import com.ozkin.microservice.firstMicroservice.repositories.StatisticsTrainerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrainingStatisticsServiceImpl")
class TrainingStatisticsServiceImplTest {

    @Mock
    private StatisticsTrainerRepository trainerRepository;

    @InjectMocks
    private TrainingStatisticsServiceImpl service;

    private static final String TRAINER_USERNAME = "trainer1";
    private static final String TRAINER_FIRST_NAME = "Juan";
    private static final String TRAINER_LAST_NAME = "Pérez";

    @Nested
    @DisplayName("addTrainingRecord")
    class AddTrainingRecord {

        @Test
        @DisplayName("creates new trainer, year and month when they do not exist")
        void shouldCreateNewTrainerYearAndMonth() {
            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainerFirstName(TRAINER_FIRST_NAME)
                    .trainerLastName(TRAINER_LAST_NAME)
                    .isActive(true)
                    .trainingDate(LocalDate.of(2025, 2, 15))
                    .trainingDuration(60)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.empty());
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            ArgumentCaptor<StatisticsTrainer> captor = ArgumentCaptor.forClass(StatisticsTrainer.class);
            verify(trainerRepository, times(2)).save(captor.capture());

            StatisticsTrainer saved = captor.getValue();
            assertThat(saved.getTrainerUsername()).isEqualTo(TRAINER_USERNAME);
            assertThat(saved.getTrainerFirstName()).isEqualTo(TRAINER_FIRST_NAME);
            assertThat(saved.getTrainerLastName()).isEqualTo(TRAINER_LAST_NAME);
            assertThat(saved.getTrainerStatus()).isTrue();
            assertThat(saved.getYears()).hasSize(1);

            TrainingYear year = saved.getYears().get(0);
            assertThat(year.getYear()).isEqualTo(2025);
            assertThat(year.getTrainingSummaryDuration()).isEqualTo(60);
            assertThat(year.getMonths()).hasSize(1);

            TrainingMonth month = year.getMonths().get(0);
            assertThat(month.getMonthNumber()).isEqualTo(2);
            assertThat(month.getDuration()).isEqualTo(60);
        }

        @Test
        @DisplayName("adds training to existing trainer and month")
        void shouldAddToExistingTrainerAndMonth() {
            TrainingMonth month = new TrainingMonth(2, 30);
            TrainingYear year = new TrainingYear(2025, 30, new ArrayList<>());
            year.getMonths().add(month);

            StatisticsTrainer existingTrainer = new StatisticsTrainer();
            existingTrainer.setId("trainer-id");
            existingTrainer.setTrainerUsername(TRAINER_USERNAME);
            existingTrainer.setYears(new ArrayList<>());
            existingTrainer.getYears().add(year);

            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainingDate(LocalDate.of(2025, 2, 20))
                    .trainingDuration(45)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.of(existingTrainer));
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            assertThat(month.getDuration()).isEqualTo(75);
            assertThat(year.getTrainingSummaryDuration()).isEqualTo(75);

            verify(trainerRepository).save(existingTrainer);
        }

        @Test
        @DisplayName("does not save when trainingDate is null")
        void shouldNotSaveWhenTrainingDateIsNull() {
            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainingDate(null)
                    .trainingDuration(60)
                    .build();

            service.addTrainingRecord(dto);

            verify(trainerRepository, never()).save(any());
            verify(trainerRepository, never()).findByTrainerUsername(any());
        }

        @Test
        @DisplayName("treats null duration as 0")
        void shouldTreatNullDurationAsZero() {
            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainingDate(LocalDate.of(2025, 3, 10))
                    .trainingDuration(null)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.empty());
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            ArgumentCaptor<StatisticsTrainer> captor = ArgumentCaptor.forClass(StatisticsTrainer.class);
            verify(trainerRepository, times(2)).save(captor.capture());

            TrainingMonth month = captor.getValue().getYears().get(0).getMonths().get(0);
            assertThat(month.getDuration()).isEqualTo(0);
        }

        @Test
        @DisplayName("null isActive defaults to true")
        void shouldDefaultIsActiveToTrueWhenNull() {
            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .isActive(null)
                    .trainingDate(LocalDate.of(2025, 1, 5))
                    .trainingDuration(30)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.empty());
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            ArgumentCaptor<StatisticsTrainer> captor = ArgumentCaptor.forClass(StatisticsTrainer.class);
            verify(trainerRepository, times(2)).save(captor.capture());

            assertThat(captor.getValue().getTrainerStatus()).isTrue();
        }

        @Test
        @DisplayName("creates new month in existing year")
        void shouldCreateNewMonthInExistingYear() {
            TrainingYear year = new TrainingYear(2025, 50, new ArrayList<>());
            year.getMonths().add(new TrainingMonth(1, 50));

            StatisticsTrainer existingTrainer = new StatisticsTrainer();
            existingTrainer.setTrainerUsername(TRAINER_USERNAME);
            existingTrainer.setYears(new ArrayList<>());
            existingTrainer.getYears().add(year);

            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainingDate(LocalDate.of(2025, 2, 10))
                    .trainingDuration(25)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.of(existingTrainer));
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            assertThat(year.getMonths()).hasSize(2);
            assertThat(year.getTrainingSummaryDuration()).isEqualTo(75);

            TrainingMonth newMonth = year.getMonths().stream()
                    .filter(m -> m.getMonthNumber() == 2)
                    .findFirst()
                    .orElseThrow();
            assertThat(newMonth.getDuration()).isEqualTo(25);
        }

        @Test
        @DisplayName("creates new year for existing trainer")
        void shouldCreateNewYearForExistingTrainer() {
            TrainingYear existingYear = new TrainingYear(2024, 100, new ArrayList<>());
            existingYear.getMonths().add(new TrainingMonth(12, 100));

            StatisticsTrainer existingTrainer = new StatisticsTrainer();
            existingTrainer.setTrainerUsername(TRAINER_USERNAME);
            existingTrainer.setYears(new ArrayList<>());
            existingTrainer.getYears().add(existingYear);

            TrainingRecordDto dto = TrainingRecordDto.builder()
                    .trainerUsername(TRAINER_USERNAME)
                    .trainingDate(LocalDate.of(2025, 1, 1))
                    .trainingDuration(90)
                    .build();

            when(trainerRepository.findByTrainerUsername(TRAINER_USERNAME))
                    .thenReturn(Optional.of(existingTrainer));
            when(trainerRepository.save(any(StatisticsTrainer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            service.addTrainingRecord(dto);

            assertThat(existingTrainer.getYears()).hasSize(2);
            assertThat(existingYear.getTrainingSummaryDuration()).isEqualTo(100);

            TrainingYear newYear = existingTrainer.getYears().stream()
                    .filter(y -> y.getYear() == 2025)
                    .findFirst()
                    .orElseThrow();
            assertThat(newYear.getTrainingSummaryDuration()).isEqualTo(90);
            assertThat(newYear.getMonths()).hasSize(1);
            assertThat(newYear.getMonths().get(0).getDuration()).isEqualTo(90);
        }
    }
}
