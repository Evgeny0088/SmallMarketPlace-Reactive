package com.marketplace.redis.starter.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.marketplace.redis.starter.TestConstants;
import com.marketplace.redis.starter.config.RedisStarterConfig;
import com.marketplace.redis.starter.config.ReceiverRecordException;
import com.marketplace.redis.starter.properties.KafkaTopicProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Service
public class KafkaConsumerListener implements TestConstants {

    private final List<User> users = new ArrayList<>();
    private final List<String> messages = new ArrayList<>();
    private final KafkaTopicProperties kafkaTopicProperties;
    private final RedisStarterConfig starterConfig;
    private final KafkaReceiver<String, String> stringReceiver;
    private final KafkaReceiver<String, TestDto> kafkaReceiverIn;
    private final KafkaReceiver<String, TestDto> kafkaReceiverOut;

    public KafkaConsumerListener(KafkaTopicProperties kafkaTopicProperties, RedisStarterConfig starterConfig) {
        this.kafkaTopicProperties = kafkaTopicProperties;
        this.starterConfig = starterConfig;
        this.stringReceiver = KafkaReceiver.create(starterConfig.createTypedReceiverOptions(kafkaTopicProperties.getTopics().get(TOPIC_MESSAGE), new TypeReference<>() {}));
        this.kafkaReceiverIn = KafkaReceiver.create(starterConfig.createTypedReceiverOptions(kafkaTopicProperties.getTopics().get(TOPIC_IN), new TypeReference<>() {}));
        this.kafkaReceiverOut = KafkaReceiver.create(starterConfig.createTypedReceiverOptions(kafkaTopicProperties.getTopics().get(TOPIC_OUT), new TypeReference<>() {}));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void getTestMessage() {
        stringReceiver.receive(1)
                .retryWhen(Retry.backoff(3, Duration.of(1, ChronoUnit.SECONDS)))
                .doOnNext(record -> {
                    record.receiverOffset().acknowledge();
                    log.info(String.format("String is processed with offset %s", record.offset()));
                    messages.add(record.value());
                })
                .log()
                .doOnError((e)-> log.error("error happened!!!!!!!!", e))
                .subscribe();
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
