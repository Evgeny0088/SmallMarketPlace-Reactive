package com.marketplace.item.storage.api.config;

import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.kafka.starter.properties.KafkaTopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import java.util.Map;

@Configuration
public class TopicConfig implements Constants {

    private final Map<String, String> topics;

    public TopicConfig(KafkaTopicProperties kafkaTopicProperties) {
        topics = kafkaTopicProperties.getTopics();
    }

    @Bean(ITEM_TRANSACTION_REQUEST_TOPIC)
    public NewTopic itemsUpdateTopic() {
        return TopicBuilder
                .name(topics.get(ITEM_TRANSACTION_REQUEST_TOPIC))
                .build();
    }

    @Bean(ITEM_TRANSACTION_RESPONSE_TOPIC)
    public NewTopic transactionResponse() {
        return TopicBuilder
                .name(topics.get(ITEM_TRANSACTION_RESPONSE_TOPIC))
                .build();
    }
}
