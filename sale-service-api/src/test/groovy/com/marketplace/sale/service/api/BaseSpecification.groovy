package com.marketplace.sale.service.api

import com.marketplace.sale.service.api.common.Constants
import com.marketplace.sale.service.api.common.TestConstants
import com.marketplace.sale.service.api.controller.RedisRepoForTest
import com.marketplace.sale.service.api.repository.RedisRepository
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import com.marketplace.test.helper.starter.testContainers.RedisContainerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST = localhost:4567"],
        classes = [Application.class]
)
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification implements Constants, TestConstants {

    @Container
    static GenericContainer redisContainer

    @Autowired
    RedisRepository redisRepository

    @Autowired
    RedisRepoForTest redisRepoForTest

    def setupSpec(){
        redisContainer = RedisContainerFactory.getRedisContainer()
    }

}
