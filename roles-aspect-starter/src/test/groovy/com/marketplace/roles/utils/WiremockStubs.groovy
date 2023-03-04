package com.marketplace.roles.utils

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.marketplace.roles.common.ApplicationConstants
import com.marketplace.roles.common.Constants
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse

class WiremockStubs implements ApplicationConstants, Constants {

    static void wireMockStubs(String mockedResponse, String url, HttpStatus status, HttpMethod method) {
        if (status == HttpStatus.OK){
            WireMock.stubFor(WireMock.request(method.name(), UrlPattern.fromOneOf(null, null, null,url))
                    .willReturn(WireMock.okJson(mockedResponse)))
        }
        else {
            WireMock.stubFor(WireMock.request(method.name(), UrlPattern.fromOneOf(null, null, null, url))
                    .willReturn(aResponse()
                            .withStatus(status.value())
                            .withBody(mockedResponse)))
        }
    }
}
