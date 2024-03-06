package com.marketplace.item.storage.api.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("r2dbc-props")
public class R2dbc_addOns_Properties {

    private String host;
    private String username;
    private String password;
    private String database;
    private int port;
}
