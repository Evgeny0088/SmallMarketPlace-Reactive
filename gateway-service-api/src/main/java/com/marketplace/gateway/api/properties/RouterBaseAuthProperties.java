package com.marketplace.gateway.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("com.marketplace.gateway.auth")
public class RouterBaseAuthProperties {

    private String marketPlaceName;
    private String marketPlacePass;
}
