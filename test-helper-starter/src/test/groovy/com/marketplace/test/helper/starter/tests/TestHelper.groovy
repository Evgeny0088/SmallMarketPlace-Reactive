package com.marketplace.test.helper.starter.tests

import com.marketplace.test.helper.starter.BaseSpecification
import com.marketplace.test.helper.starter.TestConstants
import groovy.util.logging.Slf4j
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString
import static com.marketplace.test.helper.starter.utils.HelperUtils.replaceFieldsInJsonString
import static com.marketplace.test.helper.starter.utils.HelperUtils.wireMockStubs

@Slf4j
class TestHelper extends BaseSpecification implements TestConstants {

    def "verify if containers are running"() {
        given:
        log.info("Launching of containers")

        expect:
        assert kafkaContainer.isRunning()
        assert redisContainer.isRunning()
        assert postgreSQLContainer.isRunning()
    }

    def "mock endpoint, send request with test rest template and get response"(){
        given:
        String mockedResponse = mapper.writeValueAsString(readFileAsString(RESPONSE_1))
        String requestPath = baseUrl().concat(TEST_ENDPOINT_1)

        when:
        wireMockStubs(mockedResponse, TEST_ENDPOINT_1, HttpStatus.OK, HttpMethod.GET)

        def response = restTemplate.exchange(requestPath, HttpMethod.GET, null, Object)

        then:
        assert response.getStatusCode() == HttpStatus.OK
        assert mapper.writeValueAsString(response.getBody()) == mockedResponse
    }

    def "call endpoint via web test client and get response"(){
        given:
        String requestPath = baseUrl().concat(TEST_ENDPOINT_2)

        expect:
        webTestClient
                .get()
                .uri(requestPath)
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    def "replace fields in json string" () {
        given:
        String value = VALUE
        Map<String, Object> fields = Map.of(ID, value)
        Object outputJson = mapper.readValue(replaceFieldsInJsonString(readFileAsString(inputJson), fields), Object)

        expect:
        assert outputJson[ID] == expValue

        where:
        inputJson      | expValue
        REPLACE_JSON_1 | VALUE
        REPLACE_JSON_2 | EMPTY
        REPLACE_JSON_3 | BLANK
        REPLACE_JSON_4 | null
        REPLACE_JSON_5 | null

    }
}
