package com.marketplace.kafka.starter.application;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.util.retry.Retry;

import java.util.List;

import static com.marketplace.kafka.starter.TestConstants.TOPIC_IN;

@Slf4j
@Service
public class KafkaService {

    private final NewTopic topicIn;
    private final KafkaSender<String, TestDto> kafkaSender;

    public KafkaService(@Qualifier(TOPIC_IN) NewTopic topicIn,
                        KafkaSender<String, TestDto> kafkaSender) {
        this.topicIn = topicIn;
        this.kafkaSender = kafkaSender;
    }

    public Mono<String> sendBatchOfUsers(List<SenderRecord<String, TestDto, Object>> users) {
        return this.kafkaSender
                .send(Flux.fromIterable(users))
                .retryWhen(Retry.max(3))
                .log()
                .then()
                .doOnSuccess(batch-> log.info(String.format("batch of elements (size: %s) is processed >>>", users.size())))
                .thenReturn("Processing is done");
    }

    public Mono<TestDto> sendNewUser(TestDto dto) {
        return this.kafkaSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>(topicIn.name(), dto.getId(), dto)))
                .then()
                .retryWhen(Retry.max(3))
                .doOnSuccess(item -> log.info(String.format("Object { %s } is sent to topic %s", dto, topicIn.name())))
                .doOnError(e -> log.error(String.format("Failed to send object: %s to topic: %s", dto, topicIn.name())))
                .log()
                .thenReturn(dto);
    }
}