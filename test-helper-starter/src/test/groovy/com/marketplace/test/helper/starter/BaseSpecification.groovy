package com.marketplace.test.helper.starter

import com.marketplace.test.helper.starter.common.Constants
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import com.marketplace.test.helper.starter.testContainers.KafkaContainerFactory
import com.marketplace.test.helper.starter.testContainers.PostgresContainerFactory
import com.marketplace.test.helper.starter.testContainers.RedisContainerFactory
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
        kafkaContainer = KafkaContainerFactory.getKafkaContainer()
        redisContainer = RedisContainerFactory.getRedisContainer()
        postgreSQLContainer = PostgresContainerFactory.getPostgresContainer()
    }
}
