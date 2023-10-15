package com.marketplace.kafka.starter.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.kafka.starter.TestConstants;
import com.marketplace.kafka.starter.config.StarterJsonDeserializer;
import com.marketplace.kafka.starter.properties.KafkaTopicProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Service
public class KafkaConsumerListener implements TestConstants {

    private final List<User> users = new ArrayList<>();
    private final ObjectMapper objectMapper;
    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaReceiver<String, TestDto> kafkaReceiverIn;
    private final KafkaReceiver<String, TestDto> kafkaReceiverOut;

    private final ReceiverOptions<String, TestDto> receiverOptions;

    public KafkaConsumerListener(ObjectMapper objectMapper,
                                 KafkaTopicProperties kafkaTopicProperties,
                                 ReceiverOptions<String, TestDto> receiverOptions) {
        this.objectMapper = objectMapper;
        this.kafkaTopicProperties = kafkaTopicProperties;
        this.receiverOptions = receiverOptions;
        this.kafkaReceiverIn = KafkaReceiver.create(receiverOptionsSetup(kafkaTopicProperties.getTopics().get(TOPIC_IN)));
        this.kafkaReceiverOut = KafkaReceiver.create(receiverOptionsSetup(kafkaTopicProperties.getTopics().get(TOPIC_OUT)));

    }

    private ReceiverOptions<String, TestDto> receiverOptionsSetup(String topic) {
        return this.receiverOptions
                .withValueDeserializer(new StarterJsonDeserializer<>(objectMapper, TestDto.class))
                .addAssignListener(p -> log.info(String.format(PARTITIONS_ASSIGNED, p, topic)))
                .addRevokeListener(p -> log.info(String.format(PARTITIONS_REVOKED, p, topic)))
                .subscription(Collections.singleton(topic));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void getTestItem() {
        kafkaReceiverIn.receive(1)
                .retryWhen(Retry.backoff(3, Duration.of(1, ChronoUnit.SECONDS)))
                .doOnNext(record -> {
                    record.receiverOffset().acknowledge();
                    log.info(String.format("TestDto is processed with offset %s", record.offset()));
                    users.add(User.builder()
                            .id(record.value().getId())
                            .username(record.value().getUsername())
                            .createdAt(record.value().getCreatedAt())
                            .build());
                })
                .log()
                .doOnError((e)-> log.error("error happened!!!!!!!!", e))
                .subscribe();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void getBatchTest() {
        kafkaReceiverOut.receive(1)
                .handle((record, sink)-> {
                    String value = record.value().getId();
                    int digit = Integer.parseInt(value.substring(value.lastIndexOf("-")));
                    if (digit < 20 && digit % 2 == 0) {
                        sink.error(new ReceiverRecordException(record));
                        return;
                    }
                    users.add(User.builder()
                            .id(record.value().getId())
                            .username(record.value().getUsername())
                            .build());
                    record.receiverOffset().acknowledge();
                    log.info(String.format("TestDto is processed with offset %s", record.offset()));
                })
                .onErrorContinue((e, record) -> {
                    if (e instanceof ReceiverRecordException recordException) {
                        log.error(String.format("Failed to process record: %s, with offset: %s", recordException.getRecord(), recordException.getRecord().offset()));
                        recordException.getRecord().receiverOffset().acknowledge();
                    }
                    else {
                        log.error(String.format("Error occurred in consumer: %s, with record: %s", e.getMessage(), record));
                    }})
                .log()
                .subscribe();
    }
}
