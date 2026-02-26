package com.ozkin.microservice.firstMicroservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.services.TrainingStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticsConsumer")
class StatisticsConsumerTest {

    @Mock
    private TrainingStatisticsService trainingStatisticsService;

    private ObjectMapper objectMapper;
    private StatisticsConsumer consumer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // for LocalDate
        consumer = new StatisticsConsumer(trainingStatisticsService, objectMapper);
        ReflectionTestUtils.setField(consumer, "statisticsTopic", "statistics");
    }

    @Nested
    @DisplayName("consume")
    class Consume {

        @Test
        @DisplayName("deserializes valid JSON and delegates to service")
        void shouldDeserializeAndDelegateToService() throws JsonProcessingException {
            String payload = """
                    {
                        "trainerUsername": "trainer1",
                        "trainerFirstName": "Juan",
                        "trainerLastName": "Pérez",
                        "isActive": true,
                        "trainingDate": "2025-02-15",
                        "trainingDuration": 60,
                        "actionType": "ADD"
                    }
                    """;

            consumer.consume("key-1", payload);

            ArgumentCaptor<TrainingRecordDto> captor = ArgumentCaptor.forClass(TrainingRecordDto.class);
            verify(trainingStatisticsService).addTrainingRecord(captor.capture());

            TrainingRecordDto dto = captor.getValue();
            assertThat(dto.getTrainerUsername()).isEqualTo("trainer1");
            assertThat(dto.getTrainerFirstName()).isEqualTo("Juan");
            assertThat(dto.getTrainerLastName()).isEqualTo("Pérez");
            assertThat(dto.getIsActive()).isTrue();
            assertThat(dto.getTrainingDate()).hasYear(2025).hasMonthValue(2).hasDayOfMonth(15);
            assertThat(dto.getTrainingDuration()).isEqualTo(60);
            assertThat(dto.getActionType()).isEqualTo("ADD");
        }

        @Test
        @DisplayName("throws exception when JSON is invalid")
        void shouldThrowWhenJsonIsInvalid() {
            String invalidPayload = "{ invalid json }";

            assertThatThrownBy(() -> consumer.consume("key-1", invalidPayload))
                    .isInstanceOf(Exception.class);

            verify(trainingStatisticsService, never()).addTrainingRecord(any());
        }

        @Test
        @DisplayName("propagates exception when service fails")
        void shouldPropagateExceptionWhenServiceFails() throws JsonProcessingException {
            String payload = """
                    {
                        "trainerUsername": "trainer1",
                        "trainingDate": "2025-02-15",
                        "trainingDuration": 30
                    }
                    """;

            doThrow(new RuntimeException("DB error"))
                    .when(trainingStatisticsService).addTrainingRecord(any());

            assertThatThrownBy(() -> consumer.consume("key-1", payload))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("DB error");
        }

        @Test
        @DisplayName("processes payload with minimal fields")
        void shouldProcessPayloadWithMinimalFields() throws JsonProcessingException {
            String payload = """
                    {
                        "trainerUsername": "trainer2",
                        "trainingDate": "2025-01-10"
                    }
                    """;

            consumer.consume("key-2", payload);

            ArgumentCaptor<TrainingRecordDto> captor = ArgumentCaptor.forClass(TrainingRecordDto.class);
            verify(trainingStatisticsService).addTrainingRecord(captor.capture());

            TrainingRecordDto dto = captor.getValue();
            assertThat(dto.getTrainerUsername()).isEqualTo("trainer2");
            assertThat(dto.getTrainingDate()).hasYear(2025).hasMonthValue(1).hasDayOfMonth(10);
            assertThat(dto.getTrainerFirstName()).isNull();
            assertThat(dto.getTrainerLastName()).isNull();
            assertThat(dto.getTrainingDuration()).isNull();
        }
    }
}
