package com.marketplace.item.service.api.config;

import com.marketplace.item.service.api.properties.R2dbc_addOns_Properties;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
@RequiredArgsConstructor
public class R2dbcConfiguration {
	
	private final R2dbc_addOns_Properties addOnsProperties;
	private final R2dbcProperties r2dbcProperties;
	
	@Bean
	public ConnectionFactory connectionFactory() {
		return new PostgresqlConnectionFactory(
			PostgresqlConnectionConfiguration.builder()
				.host(addOnsProperties.getHost())
				.port(addOnsProperties.getPort())
				.database(r2dbcProperties.getName())
				.username(r2dbcProperties.getUsername())
				.password(r2dbcProperties.getPassword())
				.build());
	}
	
	@Bean
	DatabaseClient client() {
		return DatabaseClient.builder()
			       .connectionFactory(connectionFactory())
			       .build();
	}
}
