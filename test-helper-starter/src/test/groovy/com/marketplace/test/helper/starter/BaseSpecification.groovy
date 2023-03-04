package com.marketplace.test.helper.starter


import com.marketplace.test.helper.starter.common.Constants
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import com.marketplace.test.helper.starter.testContainers.KafkaContainerFactory
import com.marketplace.test.helper.starter.testContainers.PostgresContainerFactory
import com.marketplace.test.helper.starter.testContainers.RedisContainerFactory
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties =["SERVICE_HOST = localhost:4567"],
        classes = [Application.class]
)
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification implements Constants {

    @Container
    static KafkaContainer kafkaContainer

    @Container
    static GenericContainer redisContainer

    @Container
    static PostgreSQLContainer postgreSQLContainer

    def setupSpec() {
        NewTopic topic1 = new NewTopic("topic1", 1, (short) 1)
        NewTopic topic2 = new NewTopic("topic2", 1, (short) 1)
        kafkaContainer = KafkaContainerFactory.getKafkaContainer(List.of(topic1, topic2))
        redisContainer = RedisContainerFactory.getRedisContainer()
        postgreSQLContainer = PostgresContainerFactory.getPostgresContainer()
    }

}
