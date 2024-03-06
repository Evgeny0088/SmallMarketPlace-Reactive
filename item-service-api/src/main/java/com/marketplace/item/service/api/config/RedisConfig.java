package com.marketplace.item.service.api.config;

import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
        if (redisProperties.getCluster() != null) {
            LettuceClientConfiguration lettuceClient = LettuceClientConfiguration.builder()
                    .readFrom(ReadFrom.REPLICA_PREFERRED)
                    .build();
            List<String> nodes = redisProperties.getCluster().getNodes();
            RedisClusterConfiguration configuration = new RedisClusterConfiguration(nodes);
            return new LettuceConnectionFactory(configuration, lettuceClient);
        }
        return lettuceConnectionFactory;
    }

    @Bean
    public ReactiveKeyCommands keyCommands() {
        return lettuceConnectionFactory().getReactiveConnection().keyCommands();
    }

    @Bean
    public ReactiveStringCommands stringCommands() {
        return lettuceConnectionFactory().getReactiveConnection().stringCommands();
    }

    @Bean
    public ReactiveZSetCommands sortedSetCommands() {
        return lettuceConnectionFactory().getReactiveConnection().zSetCommands();
    }
}
