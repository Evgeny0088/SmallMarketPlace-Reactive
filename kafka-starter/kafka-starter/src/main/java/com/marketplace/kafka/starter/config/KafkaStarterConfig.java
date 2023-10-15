package com.marketplace.kafka.starter.config;

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
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaStarterConfig implements Constants {

    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private SenderOptions<String, ?> senderOptions;
    private ReceiverOptions<String, ?> receiverOptions;

    @PostConstruct
    public void init() {
        Map<String, Object> producerProp = kafkaProperties.buildProducerProperties();
        Map<String, Object> consumerProp = kafkaProperties.buildConsumerProperties();
        producerProp.put(OBJECT_MAPPER, objectMapper);
        producerProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StarterJsonSerializer.class);
        senderOptions = SenderOptions.create(producerProp);
        receiverOptions = ReceiverOptions.create(consumerProp);
    }

    @Bean
    public KafkaSender<String, ?> kafkaSender() {
        return KafkaSender.create(senderOptions);
    }

    @Bean
    public ReceiverOptions<String, ?> receiverOptions() {
        return receiverOptions;
    }
}
