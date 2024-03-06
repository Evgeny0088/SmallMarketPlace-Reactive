package com.marketplace.kafka.starter

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.kafka.starter.config.TestConfig
import com.marketplace.test.helper.starter.testContainers.KafkaContainerFactory
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.shaded.org.awaitility.Awaitility
import org.testcontainers.shaded.org.awaitility.core.ConditionFactory
import spock.lang.Specification

import java.time.Duration

@SpringBootTest(
        classes = [KafkaTest.class])
@ActiveProfiles("test")
@Import(TestConfig.class)
class BaseSpecification extends Specification implements TestConstants, Constants {

    static ConditionFactory awaiter

    @Container
    static KafkaContainer kafkaContainer

    @Autowired
    @Qualifier(TOPIC_IN)
    NewTopic inTopic

    @Autowired
    @Qualifier(TOPIC_OUT)
    NewTopic outTopic

    @Autowired
    @Qualifier(TOPIC_MESSAGE)
    NewTopic messageTopic

    @Autowired
    AdminClient adminClient

    @Autowired
    ObjectMapper objectMapper

    def setupSpec() {
        awaiter = Awaitility.await().atMost(Duration.ofMillis(10_000))
        kafkaContainer = KafkaContainerFactory.getKafkaContainer()
    }
}
