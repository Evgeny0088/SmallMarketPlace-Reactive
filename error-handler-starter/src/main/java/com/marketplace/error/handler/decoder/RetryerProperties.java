package com.marketplace.error.handler.decoder;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "retryer")
@ConditionalOnProperty(value = "retryer.enabled", havingValue = "true")
public class RetryerProperties {

    private boolean enabled;
    private int maxAttempts;
    private int maxPeriod;
    private long backoff;

}
