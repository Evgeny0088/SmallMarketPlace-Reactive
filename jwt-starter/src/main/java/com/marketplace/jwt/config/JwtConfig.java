package com.marketplace.jwt.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.marketplace.jwt.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean("jwt-algorithm")
    Algorithm usedAlgorithm() {
        return Algorithm.HMAC256(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
