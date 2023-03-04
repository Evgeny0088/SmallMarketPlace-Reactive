package com.marketplace.gateway.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("com.marketplace.gateway")
public class RoutesProperties {

    private String pathPattern = "/.*?/(?<segment>.*)";
    private String replacementPathTemplate = "/${segment}";
    private String serverUrl;
    private String hostPrefix;
    private int port;
}
