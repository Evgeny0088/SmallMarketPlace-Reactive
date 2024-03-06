package com.marketplace.item.service.api.service;

import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.dto.ItemTransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.util.retry.Retry;

@Service
@Slf4j
public class KafkaProducerService implements Constants {

    private final NewTopic transactionResponseTopic;
    private final KafkaSender<String, Object> kafkaSender;

    public KafkaProducerService(@Qualifier(ITEM_TRANSACTION_RESPONSE_TOPIC) NewTopic transactionResponseTopic, KafkaSender<String, Object> kafkaSender) {
        this.transactionResponseTopic = transactionResponseTopic;
        this.kafkaSender = kafkaSender;
    }

    public Mono<ItemTransactionResponse> sendTransactionResponse(ItemTransactionResponse response) {
        return this.kafkaSender.createOutbound()
                .send(Mono.just(new ProducerRecord<>(transactionResponseTopic.name(), response.getTransactionId(), response)))
                .then()
                .retryWhen(Retry.max(3))
                .doOnSuccess(item -> log.info(String.format("Object { %s } is sent to topic %s", response, transactionResponseTopic.name())))
                .doOnError(e -> log.error(String.format("Failed to send object: %s to topic: %s", response, transactionResponseTopic.name())))
                .log()
                .thenReturn(response);
    }
}
