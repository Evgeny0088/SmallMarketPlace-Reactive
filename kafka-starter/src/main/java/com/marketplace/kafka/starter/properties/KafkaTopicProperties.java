package com.marketplace.kafka.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.topics")
public class KafkaTopicProperties {

    int partitions;
    int replicas;
    Map<String, String> topics = new HashMap<>();

}
