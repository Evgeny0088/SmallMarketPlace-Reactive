package com.marketplace.gateway.api.controller

import com.marketplace.gateway.api.BaseSpecification
import com.marketplace.gateway.api.TestConstants
import com.marketplace.gateway.api.common.Constants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Stepwise

import java.util.function.Consumer

import static com.marketplace.gateway.api.utils.HelpTestMethods.setupHeaders
import static com.marketplace.gateway.api.utils.HelpTestMethods.verifyResult
import static com.marketplace.gateway.api.utils.HelpTestMethods.wireMockStubs
import static com.marketplace.gateway.api.utils.TokenTestUtils.createTestToken
import static com.marketplace.gateway.api.utils.TokenTestUtils.setupExpiredJwtProperties
import static com.marketplace.gateway.api.utils.TokenTestUtils.setupWrongSecretJwtProperties
import static com.marketplace.gateway.api.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString

@Stepwise
class GatewayControllerSpec extends BaseSpecification implements Constants, TestConstants {

    String requestPath

    def setup() {
        requestPath = baseUrl().concat("/%s")
        jwtProperties.setSecret(TOKEN_TEST_SECRET)
        jwtProperties.setTokenExpiresAt(TOKEN_EXPIRES_AFTER_DEFAULT_PROPERTY)
        wireMockServer.resetAll()
    }

    def "Gateway incoming requests filtering"() {
        given:
        String requestPath = String.format(requestPath, incomingUrl)
        String requestJson = requestBody == null ? null : readFileAsString(requestBody as String)
        String responseJson = readFileAsString(RESPONSE_200)
        HttpMethod httpMethod = requestJson == null ? HttpMethod.GET : HttpMethod.POST

        String token = tokenProvied ? createTestToken(CORRECT, jwtService) : null
        Consumer<HttpHeaders> headers = setupHeaders(clientName, clientPass, token)
        wireMockStubs(responseJson, expectedUrl, statusCode, httpMethod)

        when:
        def response = callServiceForResponse(httpMethod, statusCode, webTestClient, requestPath, headers, requestJson)

        then:
        verifyResult(response, readFileAsString(expectedResponse))

        where:
        requestBody     | tokenProvied | clientName  | clientPass  | incomingUrl         | expectedUrl                  | expectedResponse | statusCode
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_1 | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_2 | TEST_INCOMING_URL_2_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_3 | TEST_INCOMING_URL_3_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_4 | TEST_INCOMING_URL_4_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_5 | TEST_INCOMING_URL_5_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_6 | TEST_INCOMING_URL_6_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_7 | TEST_INCOMING_URL_7_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_8 | TEST_INCOMING_URL_8_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_9 | TEST_INCOMING_URL_9_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_10| TEST_INCOMING_URL_10_EXPECTED| RESPONSE_200     | HttpStatus.OK

        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_30 | TEST_INCOMING_URL_30_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_31 | TEST_INCOMING_URL_31_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_32 | TEST_INCOMING_URL_32_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_33 | TEST_INCOMING_URL_33_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_34 | TEST_INCOMING_URL_34_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_35 | TEST_INCOMING_URL_35_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_36 | TEST_INCOMING_URL_36_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_37 | TEST_INCOMING_URL_37_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        SERVICE_REQUEST | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_38 | TEST_INCOMING_URL_38_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_39 | TEST_INCOMING_URL_39_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND

        SERVICE_REQUEST | false        | CLIENT_NAME | CLIENT_PASS | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_1 | HttpStatus.UNAUTHORIZED
        null            | true         | null        | CLIENT_PASS | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | true         | CLIENT_NAME | null        | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        null            | true         | INVALID     | CLIENT_PASS | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | true         | CLIENT_NAME | INVALID     | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        null            | true         | null        | INVALID     | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | true         | INVALID     | null        | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
        null            | true         | null        | null        | TEST_INCOMING_URL_1  | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2 | HttpStatus.UNAUTHORIZED
    }

    def "verify auth-not-required paths" () {
        given:
        String requestPath = String.format(requestPath, incomingUrl)
        String requestJson = requestBody == null ? null : readFileAsString(requestBody as String)
        String responseJson = readFileAsString(RESPONSE_200)
        HttpMethod httpMethod = requestJson == null ? HttpMethod.GET : HttpMethod.POST

        String token = tokenProvied ? createTestToken(CORRECT, jwtService) : null
        Consumer<HttpHeaders> headers = setupHeaders(clientName, clientPass, token)
        wireMockStubs(responseJson, expectedUrl, statusCode, httpMethod)

        when:
        def response = callServiceForResponse(httpMethod, statusCode, webTestClient, requestPath, headers, requestJson)

        then:
        verifyResult(response, readFileAsString(expectedResponse))

        where:
        requestBody          | tokenProvied | clientName   | clientPass  | incomingUrl         | expectedUrl                  | expectedResponse | statusCode
        SERVICE_REQUEST      | true         | CLIENT_NAME  | CLIENT_PASS | OPEN_INCOMING_URL_1 | OPEN_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null                 | true         | INVALID      | CLIENT_PASS | OPEN_INCOMING_URL_1 | OPEN_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST      | false        | INVALID      | CLIENT_PASS | OPEN_INCOMING_URL_2 | OPEN_INCOMING_URL_2_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null                 | false        | CLIENT_NAME  | INVALID     | OPEN_INCOMING_URL_2 | OPEN_INCOMING_URL_2_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST      | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_3 | OPEN_INCOMING_URL_3_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null                 | true         | CLIENT_NAME  | INVALID     | OPEN_INCOMING_URL_4 | OPEN_INCOMING_URL_4_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST      | true         | CLIENT_NAME  | INVALID     | OPEN_INCOMING_URL_9 | OPEN_INCOMING_URL_9_EXPECTED | RESPONSE_200     | HttpStatus.OK

        null                 | true         | INVALID      | CLIENT_PASS | TEST_INCOMING_URL_1 | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_401_2   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST      | true         | CLIENT_NAME  | INVALID     | OPEN_INCOMING_URL_5 | OPEN_INCOMING_URL_5_EXPECTED | RESPONSE_401_2   | HttpStatus.UNAUTHORIZED
        null                 | false        | CLIENT_NAME  | CLIENT_PASS | OPEN_INCOMING_URL_6 | OPEN_INCOMING_URL_6_EXPECTED | RESPONSE_401_1   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST      | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_7 | OPEN_INCOMING_URL_7_EXPECTED | RESPONSE_401_2   | HttpStatus.UNAUTHORIZED
        null                 | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_8 | OPEN_INCOMING_URL_8_EXPECTED | RESPONSE_401_2   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST      | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_8 | OPEN_INCOMING_URL_8_EXPECTED | RESPONSE_401_2   | HttpStatus.UNAUTHORIZED
        null                 | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_13| OPEN_INCOMING_URL_13_EXPECTED| RESPONSE_401_2   | HttpStatus.UNAUTHORIZED

        SERVICE_REQUEST      | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_10 | OPEN_INCOMING_URL_10_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        null                 | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_11 | OPEN_INCOMING_URL_11_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND
        SERVICE_REQUEST      | true         | INVALID      | INVALID     | OPEN_INCOMING_URL_12 | OPEN_INCOMING_URL_12_EXPECTED| RESPONSE_404    | HttpStatus.NOT_FOUND

    }

    def "verify open-api paths" () {
        given:
        String requestPath = String.format(requestPath, incomingUrl)
        String requestJson = requestBody == null ? null : readFileAsString(requestBody as String)
        String responseJson = readFileAsString(RESPONSE_200)
        HttpMethod httpMethod = requestJson == null ? HttpMethod.GET : HttpMethod.POST

        String token = tokenProvied ? createTestToken(CORRECT, jwtService) : null
        Consumer<HttpHeaders> headers = setupHeaders(clientName, clientPass, token)
        wireMockStubs(responseJson, expectedUrl, statusCode, httpMethod)

        when:
        def response = callServiceForResponse(httpMethod, statusCode, webTestClient, requestPath, headers, requestJson)

        then:
        verifyResult(response, readFileAsString(expectedResponse))

        where:
        requestBody     | tokenProvied | clientName   | clientPass  | incomingUrl           | expectedUrl                    | expectedResponse | statusCode
        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_1 | CLOSED_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_2 | CLOSED_INCOMING_URL_2_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_3 | CLOSED_INCOMING_URL_3_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_4 | CLOSED_INCOMING_URL_4_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_5 | CLOSED_INCOMING_URL_5_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_6 | CLOSED_INCOMING_URL_6_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_7 | CLOSED_INCOMING_URL_7_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_8 | CLOSED_INCOMING_URL_8_EXPECTED | RESPONSE_200     | HttpStatus.OK

        SERVICE_REQUEST | false        | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_1 | CLOSED_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | INVALID      | INVALID     | CLOSED_INCOMING_URL_1 | CLOSED_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        SERVICE_REQUEST | false        | INVALID      | INVALID     | CLOSED_INCOMING_URL_1 | CLOSED_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK
        null            | true         | INVALID      | CLIENT_PASS | CLOSED_INCOMING_URL_1 | CLOSED_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK

        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_20| CLOSED_INCOMING_URL_20_EXPECTED| RESPONSE_404     | HttpStatus.NOT_FOUND
        null            | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_21| CLOSED_INCOMING_URL_21_EXPECTED| RESPONSE_404     | HttpStatus.NOT_FOUND
        SERVICE_REQUEST | true         | CLIENT_NAME  | CLIENT_PASS | CLOSED_INCOMING_URL_22| CLOSED_INCOMING_URL_22_EXPECTED| RESPONSE_404     | HttpStatus.NOT_FOUND

    }

    def "verify token exceptions" () {
        given:
        String requestPath = String.format(requestPath, incomingUrl)
        String requestJson = requestBody == null ? null : readFileAsString(requestBody as String)
        String responseJson = readFileAsString(RESPONSE_200)
        HttpMethod httpMethod = requestJson == null ? HttpMethod.GET : HttpMethod.POST

        setupExpiredJwtProperties(jwtProperties, tokenCondition)
        Consumer<HttpHeaders> headers = setupHeaders(CLIENT_NAME, CLIENT_PASS, createTestToken(tokenCondition, jwtService))
        wireMockStubs(responseJson, expectedUrl, statusCode, httpMethod)
        setupWrongSecretJwtProperties(jwtProperties, tokenCondition)

        if (tokenCondition == EXPIRED) Thread.sleep(1000)

        when:
        def response = callServiceForResponse(httpMethod, statusCode, webTestClient, requestPath, headers, requestJson)

        then:
        verifyResult(response, readFileAsString(expectedResponse))

        where:
        requestBody     | tokenCondition | incomingUrl         | expectedUrl                  | expectedResponse | statusCode
        SERVICE_REQUEST | CORRECT        | TEST_INCOMING_URL_1 | TEST_INCOMING_URL_1_EXPECTED | RESPONSE_200     | HttpStatus.OK

        null            | EMPTY_T        |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_1   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | BLANK_T        |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_1   | HttpStatus.UNAUTHORIZED
        null            | WRONG_SIGN     |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_3   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | INVALID_SECRET |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_4   | HttpStatus.UNAUTHORIZED
        null            | WRONG_ALG      |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_5   | HttpStatus.UNAUTHORIZED
        SERVICE_REQUEST | EXPIRED        |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_6   | HttpStatus.UNAUTHORIZED
        null            | BAD_FORMATTING |TEST_INCOMING_URL_1  |TEST_INCOMING_URL_1_EXPECTED  | RESPONSE_401_7   | HttpStatus.UNAUTHORIZED
    }
}
