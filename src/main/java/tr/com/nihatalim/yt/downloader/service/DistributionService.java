package tr.com.nihatalim.yt.downloader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import tr.com.nihatalim.yt.core.dto.DownloadProgressDto;
import tr.com.nihatalim.yt.core.enums.TopicEnum;
import tr.com.nihatalim.yt.downloader.exception.KafkaProduceFailedException;

@Slf4j
@Service
public class DistributionService {
    private final KafkaTemplate<Long, DownloadProgressDto> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.prefix}")
    private String topicPrefix;

    public DistributionService(KafkaTemplate<Long, DownloadProgressDto> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(TopicEnum topicEnum, DownloadProgressDto item) {
        try {
            kafkaTemplate.send(topicPrefix + topicEnum.getTopicName(), item).addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    throw new KafkaProduceFailedException(String.format("[DistributionService] Cannot produced to topic: %s", topicEnum.getTopicName()));
                }

                @Override
                public void onSuccess(SendResult<Long, DownloadProgressDto> result) {
                    log.info("[DistributionService] Producer success for topic: {} and for item: {}", result.getRecordMetadata().topic(), result.getProducerRecord().value());
                }
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new KafkaProduceFailedException(String.format("[DistributionService] Cannot produced to topic: %s", topicEnum.getTopicName()));
        }
    }
}
