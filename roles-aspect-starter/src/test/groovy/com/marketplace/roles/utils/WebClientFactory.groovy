package com.marketplace.roles.utils

import com.marketplace.roles.common.ApplicationConstants
import com.marketplace.roles.common.Constants
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

import java.util.function.Consumer

class WebClientFactory implements ApplicationConstants, Constants{

    static EntityExchangeResult<byte[]> callServiceForResponse(HttpMethod httpMethod,
                                                               HttpStatus status,
                                                               WebTestClient webTestClient,
                                                               String endpoint,
                                                               String ownerId,
                                                               Map<String, String> headerFields,
                                                               String filePath,
                                                               Object requestBody){
        if (httpMethod == HttpMethod.GET) {
            if (status == HttpStatus.OK) {
                return webclientExchange_GET_200(webTestClient, endpoint, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
            if (status == HttpStatus.BAD_REQUEST) {
                return webclientExchange_GET_400(webTestClient, endpoint, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
            if (status == HttpStatus.FORBIDDEN) {
                return webclientExchange_GET_403(webTestClient, endpoint, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
        }

        if (httpMethod == HttpMethod.POST && requestBody != null) {
            if (status == HttpStatus.OK){
                return webclientExchange_POST_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_POST_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
        }

        if (httpMethod == HttpMethod.POST && filePath != null){
            if (status == HttpStatus.OK){
                return webclientExchange_POST_WITH_REQUEST_PARTS_200(webTestClient, endpoint, filePath, ownerId, headersSetup(MediaType.MULTIPART_FORM_DATA, headerFields))
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_POST_WITH_REQUEST_PARTS_400(webTestClient, endpoint, filePath, ownerId, headersSetup(MediaType.MULTIPART_FORM_DATA, headerFields))
            }
        }

        if (httpMethod == HttpMethod.PATCH && requestBody != null) {
            if (status == HttpStatus.OK){
                return webclientExchange_PATCH_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_PATCH_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
        }

        if (httpMethod == HttpMethod.PUT && requestBody != null) {
            if (status == HttpStatus.OK){
                return webclientExchange_PUT_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_PUT_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headersSetup(MediaType.APPLICATION_JSON, headerFields))
            }
        }
        return null
    }

    private static Consumer<HttpHeaders> headersSetup(MediaType mediaType, Map<String, String> headerPairs) {
        return (headers) -> {
            headers.setContentType(mediaType)
            headerPairs.entrySet().forEach(entry -> headers.add(entry.getKey(), entry.getValue()))
        }
    }

    private static EntityExchangeResult<byte[]> webclientExchange_GET_200(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .get()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_GET_400(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .get()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isBadRequest().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_200(WebTestClient webTestClient,
                                                                                             String requestUrl,
                                                                                             Object requestBody,
                                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_400(WebTestClient webTestClient,
                                                                                             String requestUrl,
                                                                                             Object requestBody,
                                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isBadRequest().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_PATCH_WITH_REQUEST_BODY_200(WebTestClient webTestClient,
                                                                                              String requestUrl,
                                                                                              Object requestBody,
                                                                                              Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .patch()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_PATCH_WITH_REQUEST_BODY_400(WebTestClient webTestClient,
                                                                                              String requestUrl,
                                                                                              Object requestBody,
                                                                                              Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .patch()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isBadRequest().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_PUT_WITH_REQUEST_BODY_200(WebTestClient webTestClient,
                                                                                            String requestUrl,
                                                                                            Object requestBody,
                                                                                            Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .put()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_PUT_WITH_REQUEST_BODY_400(WebTestClient webTestClient,
                                                                                            String requestUrl,
                                                                                            Object requestBody,
                                                                                            Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .put()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isBadRequest().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_PARTS_200(WebTestClient webTestClient,
                                                                                              String requestUrl,
                                                                                              String filePath,
                                                                                              String ownerId,
                                                                                              Consumer<HttpHeaders> headersConsumer) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder()
        bodyBuilder.part(FILE, new ClassPathResource(filePath))
        bodyBuilder.part(FIELD, ownerId)

        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(bodyBuilder.build()))
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_PARTS_400(WebTestClient webTestClient,
                                                                                              String requestUrl,
                                                                                              String filePath,
                                                                                              String ownerId,
                                                                                              Consumer<HttpHeaders> headersConsumer) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder()
        bodyBuilder.part(FILE, new ClassPathResource(filePath))
        bodyBuilder.part(FIELD, ownerId)

        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(bodyBuilder.build()))
                .exchange()
                .expectStatus().isBadRequest().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_GET_403(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .get()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isForbidden().expectBody().returnResult()
    }
}
