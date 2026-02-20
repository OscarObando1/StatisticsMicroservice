package com.ozkin.microservice.firstMicroservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozkin.microservice.firstMicroservice.dtos.TrainingRecordDto;
import com.ozkin.microservice.firstMicroservice.services.TrainingStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticsConsumer {

    private final TrainingStatisticsService trainingStatisticsService;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.statistics:statistics}")
    private String statisticsTopic;

    @KafkaListener(
            topics = "${spring.kafka.topic.statistics:statistics}",
            containerFactory = "statisticKafkaListenerContainerFactory"
    )
    public void consume(String key, String payload) throws JsonProcessingException {
        try {
            TrainingRecordDto dto = objectMapper.readValue(payload, TrainingRecordDto.class);
            log.info("Message received  topic {} - trainer: {}", statisticsTopic, dto.getTrainerUsername());
            trainingStatisticsService.addTrainingRecord(dto);
            log.debug("TrainingRecordDto processed {}", dto.getTrainerUsername());
        } catch (Exception ex) {
            log.error("Error processed message Kafka (key: {}): {}", key, ex.getMessage(), ex);
            throw ex;
        }
    }
}
