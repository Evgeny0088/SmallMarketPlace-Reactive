package com.marketplace.kafka.starter.config

import com.marketplace.kafka.starter.TestConstants
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig implements TestConstants {

    @Bean
    AdminClient adminClient(KafkaProperties kafkaProperties) {
        return AdminClient.create(kafkaProperties.buildAdminProperties());
    }
}
