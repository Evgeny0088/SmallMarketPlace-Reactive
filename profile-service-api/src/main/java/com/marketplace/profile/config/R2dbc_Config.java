package com.marketplace.profile.config;

import com.marketplace.profile.properties.R2dbc_Properties;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@RequiredArgsConstructor
public class R2dbc_Config {

    private final R2dbc_Properties r2DBC_properties;

    @Bean
    PostgresqlConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(r2DBC_properties.getHost())
                        .port(r2DBC_properties.getPort())
                        .database(r2DBC_properties.getDatabase())
                        .username(r2DBC_properties.getUsername())
                        .password(r2DBC_properties.getPassword())
                        .build());
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .build();
    }
}
