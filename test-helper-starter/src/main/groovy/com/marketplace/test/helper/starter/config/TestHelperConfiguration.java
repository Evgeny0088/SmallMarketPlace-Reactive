package com.marketplace.test.helper.starter.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.marketplace.test.helper.starter.common.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Configuration
@Profile("test")
public class TestHelperConfiguration implements Constants {

    @Bean(TEST_WIREMOCK_SERVER)
    @ConditionalOnMissingBean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(options().port(SERVER_PORT));
        WireMock.configureFor(SERVER_HOST, SERVER_PORT);
        return wireMockServer;
    }

    @Bean(TEST_REST_TEMPLATE)
    @ConditionalOnMissingBean
    public TestRestTemplate restTemplate() {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.getRestTemplate().getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
