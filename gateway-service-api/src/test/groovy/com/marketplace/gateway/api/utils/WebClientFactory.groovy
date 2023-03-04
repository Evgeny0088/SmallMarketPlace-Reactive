package com.marketplace.gateway.api.utils

import com.marketplace.gateway.api.TestConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

import java.util.function.Consumer

class WebClientFactory implements TestConstants {

    static EntityExchangeResult<byte[]> callServiceForResponse(HttpMethod httpMethod,
                                                               HttpStatus status,
                                                               WebTestClient webTestClient,
                                                               String endpoint,
                                                               Consumer<HttpHeaders> headers,
                                                               Object requestBody) {
        if (httpMethod == HttpMethod.GET) {
            if (status == HttpStatus.OK) {
                return webclientExchange_GET_200(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.BAD_REQUEST) {
                return webclientExchange_GET_400(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.NOT_FOUND) {
                return webclientExchange_GET_404(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.UNAUTHORIZED) {
                return webclientExchange_GET_401(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.FORBIDDEN) {
                return webclientExchange_GET_403(webTestClient, endpoint, headers)
            }
        }

        if (httpMethod == HttpMethod.POST && requestBody != null) {
            if (status == HttpStatus.OK){
                return webclientExchange_POST_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.NOT_FOUND) {
                return webclientExchange_POST_WITH_REQUEST_BODY_404(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_POST_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.UNAUTHORIZED) {
                return webclientExchange_POST_WITH_REQUEST_BODY_401(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.SERVICE_UNAVAILABLE){
                return webclientExchange_POST_WITH_REQUEST_BODY_503(webTestClient, endpoint, requestBody, headers)
            }
        }
        return null
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

    private static EntityExchangeResult<byte[]> webclientExchange_GET_404(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .get()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isNotFound().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_GET_401(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .get()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isUnauthorized().expectBody().returnResult()
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

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_503(WebTestClient webTestClient,
                                                                                             String requestUrl,
                                                                                             Object requestBody,
                                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().is5xxServerError().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_404(WebTestClient webTestClient,
                                                                                             String requestUrl,
                                                                                             Object requestBody,
                                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isNotFound().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_401(WebTestClient webTestClient,
                                                                                             String requestUrl,
                                                                                             Object requestBody,
                                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isUnauthorized().expectBody().returnResult()
    }
}
