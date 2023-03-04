package com.marketplace.test.helper.starter.utils

import com.github.tomakehurst.wiremock.matching.UrlPattern
import com.marketplace.test.helper.starter.common.Constants
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.logging.log4j.util.Strings
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.ResourceUtils

import static com.github.tomakehurst.wiremock.client.WireMock.*

class HelperUtils implements Constants {

    static String readFileAsString(String path) {
        return ResourceUtils.getFile(CLASSPATH.concat(path)).text
    }

    static String addFieldsToJsonString(String jsonString, Map<String, Object> fields) {
        def json = new JsonSlurper().parseText(jsonString)
        fields.each {json << (["${it.key}" : it.value])}
        return JsonOutput.toJson(json)
    }

    static String replaceFieldsInJsonString(String jsonString, Map<String, Object> fields) {
        def slurped = new JsonSlurper().parseText(jsonString)
        def builder = new JsonBuilder(slurped)
        fields.each {
            def key = builder.content[it.key]
            if (Strings.isNotBlank(key)) {
                builder.content[it.key] = it.value
            }
        }
        return builder.toPrettyString()
    }

    static void wireMockStubs(String mockedResponse, String url, HttpStatus status, HttpMethod method) {
        if (status == HttpStatus.OK){
            stubFor(request(method.name(), UrlPattern.fromOneOf(null, null, null,url))
                    .willReturn(okJson(mockedResponse)))
        }
        else {
            stubFor(request(method.name(), UrlPattern.fromOneOf(null, null, null, url))
                    .willReturn(aResponse()
                            .withStatus(status.value())
                            .withBody(mockedResponse)))
        }
    }
}
