package com.marketplace.test.helper.starter.specification

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.marketplace.test.helper.starter.common.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@AutoConfigureWebTestClient(timeout = WEBCLIENT_TIMEOUT)
abstract class AbstractSpecification extends Specification implements Constants {

    @Autowired
    ObjectMapper testObjectMapper

    @Autowired
    @Qualifier(TEST_WIREMOCK_SERVER)
    WireMockServer wireMockServer

    @Autowired
    @Qualifier(TEST_REST_TEMPLATE)
    TestRestTemplate restTemplate

    @Autowired
    WebTestClient webTestClient

    void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory())
        wireMockServer.start()
    }

    void cleanup() {
        wireMockServer.stop()
    }

    String baseUrl() {
        return "http://${SERVER_HOST}:${SERVER_PORT}"
    }
}
