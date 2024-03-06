package com.marketplace.redis.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.redis.starter.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
@AutoConfigureBefore(RedisReactiveAutoConfiguration.class)
public class RedisStarterConfig implements Constants {

    private final RedisProperties redisProperties;

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }


}
