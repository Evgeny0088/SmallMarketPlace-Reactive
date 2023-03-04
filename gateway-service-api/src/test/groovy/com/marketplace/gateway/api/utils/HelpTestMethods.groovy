package com.marketplace.gateway.api.utils

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.marketplace.ProfileDto
import com.marketplace.gateway.api.common.Constants
import com.marketplace.jwt.jwtservice.JWTService
import com.marketplace.jwt.properties.JwtProperties
import lombok.extern.slf4j.Slf4j
import org.apache.http.entity.ContentType
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import com.marketplace.gateway.api.TestConstants
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult

import java.nio.charset.StandardCharsets
import java.util.function.Consumer

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse

@Slf4j
class HelpTestMethods implements TestConstants, Constants{

    static void wireMockStubs(String mockedResponse, String url, HttpStatus status, HttpMethod method) {
        if (status == HttpStatus.OK){
            WireMock.stubFor(WireMock.request(method.name(), UrlPattern.fromOneOf(null, null, url, null))
                .willReturn(WireMock.okJson(mockedResponse)))
        }
        else {
            WireMock.stubFor(WireMock.request(method.name(), UrlPattern.fromOneOf(null, null, url, null))
                .willReturn(aResponse()
                    .withHeader(CONTENT_TYPE,ContentType.APPLICATION_JSON.toString())
                    .withStatus(status.value())
                    .withBody(mockedResponse)))
        }
    }

    static Consumer<HttpHeaders> setupHeaders(String clientName, String clientPass, String token) {
        return (headers) -> {
            headers.add(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            headers.add(MARKETPLACE_HEADER_NAME, clientName)
            headers.add(MARKETPLACE_HEADER_PASS, clientPass)
            headers.add(HEADER_ACCESS_TOKEN, token)
        }
    }

    static void verifyResult(EntityExchangeResult<byte[]> response, String expectedResponse) {
        String actualResponse = new String(response.getResponseBody(), StandardCharsets.UTF_8)
        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.NON_EXTENSIBLE)
    }
}