package com.marketplace.kafka.starter.application;

import com.marketplace.kafka.starter.TestConstants;
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

@Slf4j
@Service
public class KafkaService implements TestConstants  {

    private final NewTopic topicIn;
    private final NewTopic messageTopic;
    private final KafkaSender<String, Object> kafkaSender;

    public KafkaService(@Qualifier(TOPIC_IN) NewTopic topicIn,
                        @Qualifier(TOPIC_MESSAGE) NewTopic messageTopic,
                        KafkaSender<String, Object> kafkaSender) {
        this.topicIn = topicIn;
        this.messageTopic = messageTopic;
        this.kafkaSender = kafkaSender;
    }

    public Mono<String> sendBatchOfUsers(List<SenderRecord<String, Object, Object>> users) {
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

    public Mono<String> sendMessage(String message) {
        return this.kafkaSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>(messageTopic.name(), message, message)))
                .then()
                .retryWhen(Retry.max(3))
                .doOnSuccess(item -> log.info(String.format("Object { %s } is sent to topic %s", message, topicIn.name())))
                .doOnError(e -> log.error(String.format("Failed to send object: %s to topic: %s", message, topicIn.name())))
                .log()
                .thenReturn(message);
    }
}