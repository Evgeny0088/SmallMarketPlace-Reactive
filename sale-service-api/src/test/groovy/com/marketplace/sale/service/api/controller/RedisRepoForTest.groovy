package com.marketplace.sale.service.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import lombok.SneakyThrows
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.ReactiveZSetCommands
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

import java.nio.ByteBuffer

@Slf4j
@Repository
@Profile("test")
class RedisRepoForTest {

    private final ObjectMapper mapper
    private final ReactiveZSetCommands sortedCommands

    RedisRepoForTest(ObjectMapper mapper, ReactiveZSetCommands sortedCommands) {
        this.mapper = mapper
        this.sortedCommands = sortedCommands
    }

    @SneakyThrows
    Mono<Long> appendToCollection(ByteBuffer byteKey, double score, Object details) {
        return sortedCommands.zAdd(
                byteKey,
                score,
                ByteBuffer.wrap(mapper.writeValueAsBytes(details)))
    }
}
