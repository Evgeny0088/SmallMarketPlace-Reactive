package com.marketplace.kafka.starter.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.kafka.starter.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaStarterConfig implements Constants {

    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private SenderOptions<String, ?> senderOptions;

    @PostConstruct
    public void init() {
        Map<String, Object> producerProp = kafkaProperties.buildProducerProperties();
        producerProp.put(OBJECT_MAPPER, objectMapper);
        producerProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StarterJsonSerializer.class);
        senderOptions = SenderOptions.create(producerProp);
    }

    @Bean
    public KafkaSender<String, ?> kafkaSender() {
        return KafkaSender.create(senderOptions);
    }

    public <T> ReceiverOptions<String, T> createTypedReceiverOptions(String topic, TypeReference<T> object) {
        Map<String, Object> consumerProp = kafkaProperties.buildConsumerProperties();
        consumerProp.put(OBJECT_MAPPER, objectMapper);
        consumerProp.put(TYPE_REFERENCE, object);
        ReceiverOptions<String, T> receiver = ReceiverOptions.create(consumerProp);
        return receiver
                .withValueDeserializer(new StarterJsonDeserializer<>(consumerProp))
                .addAssignListener(p -> log.info(String.format(PARTITIONS_ASSIGNED, p, topic)))
                .addRevokeListener(p -> log.info(String.format(PARTITIONS_REVOKED, p, topic)))
                .subscription(Collections.singleton(topic));
    }
}
