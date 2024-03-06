package com.marketplace.sale.service.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.sale.service.api.controller.RedisRepoForTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.ReactiveZSetCommands

@TestConfiguration
class TestConfig {

    @Bean
    RedisRepoForTest redisRepoForTest(ObjectMapper mapper, ReactiveZSetCommands commands) {
        return new RedisRepoForTest(mapper, commands)
    }
}
