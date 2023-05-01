package com.marketplace.config.starter

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.marketplace.config.starter.common.ApplicationConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST=localhost:4567"],
        classes = [Application.class])
@AutoConfigureWebTestClient(timeout = "1000000")
@ActiveProfiles("test")
class BaseSpecification extends Specification implements ApplicationConstants{

    @Autowired
    ObjectMapper mapper

    @Autowired
    WebTestClient webTestClient

    WireMockServer wireMockServer

    @Shared
    static String baseUrl

    void setupSpec(){
        baseUrl = "http://${TEST_HOST}:${TEST_PORT}"
    }

    void setup() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(TEST_PORT))
        wireMockServer.start()
        WireMock.configureFor(TEST_HOST, TEST_PORT)
    }

    void cleanup() {
        wireMockServer.stop()
    }
}
