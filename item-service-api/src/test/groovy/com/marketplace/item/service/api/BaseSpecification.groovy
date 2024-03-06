package com.marketplace.item.service.api


import com.marketplace.item.service.api.common.Constants
import com.marketplace.item.service.api.common.TestConstants
import com.marketplace.item.service.api.repository.BrandRepository
import com.marketplace.item.service.api.repository.ItemRepository
import com.marketplace.item.service.api.repository.SellTransactionRepository
import com.marketplace.item.service.api.repository.StandAloneRepository
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import com.marketplace.test.helper.starter.testContainers.KafkaContainerFactory
import com.marketplace.test.helper.starter.testContainers.PostgresContainerFactory
import com.marketplace.test.helper.starter.testContainers.RedisContainerFactory
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST = localhost:4567"],
        classes = [Application.class]
)
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification implements Constants, TestConstants {

    @Container
    static PostgreSQLContainer postgreSQLContainer

    @Container
    static GenericContainer redisContainer

    @Container
    static KafkaContainer kafkaContainer

    @Autowired
    @Qualifier(ITEM_TRANSACTION_REQUEST_TOPIC)
    NewTopic itemRequestTopic

    @Autowired
    @Qualifier(ITEM_TRANSACTION_RESPONSE_TOPIC)
    NewTopic itemResponseTopic

    @Autowired
    BrandRepository brandRepository

    @Autowired
    ItemRepository itemRepository

    @Autowired
    StandAloneRepository redisRepository

    @Autowired
    SellTransactionRepository transactionRepository

    @Autowired
    DatabaseClient databaseClient

    def setupSpec(){
        postgreSQLContainer = PostgresContainerFactory.getPostgresContainer()
        redisContainer = RedisContainerFactory.getRedisContainer()
        kafkaContainer = KafkaContainerFactory.getKafkaContainer()
    }

}
