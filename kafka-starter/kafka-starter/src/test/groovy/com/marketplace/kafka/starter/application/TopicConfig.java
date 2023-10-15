package com.marketplace.kafka.starter.application;

import com.marketplace.kafka.starter.TestConstants;
import com.marketplace.kafka.starter.properties.KafkaTopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class TopicConfig implements TestConstants {

    private final int partitions;
    private final int replicas;
    private final Map<String, String> topics;

    public TopicConfig(KafkaTopicProperties kafkaTopicProperties) {
        partitions = kafkaTopicProperties.getPartitions();
        replicas = kafkaTopicProperties.getReplicas();
        topics = kafkaTopicProperties.getTopics();
    }

    @Bean(TOPIC_IN)
    public NewTopic inTopic() {
        return TopicBuilder
                .name(topics.get(TOPIC_IN))
                .replicas(replicas)
                .partitions(partitions)
                .build();
    }

    @Bean(TOPIC_OUT)
    public NewTopic outTopic() {
        return TopicBuilder
                .name(topics.get(TOPIC_OUT))
                .replicas(replicas)
                .partitions(partitions)
                .build();
    }
}
