package com.marketplace.item.storage.api.utils

import com.marketplace.item.storage.api.common.Constants
import com.marketplace.item.storage.api.common.TestConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

import java.util.function.Consumer

class WebClientFactory implements TestConstants, Constants {

    static Consumer<HttpHeaders> headersSetup(MediaType mediaType, String profileId, String roles) {
        return (headers) -> {
            headers.setContentType(mediaType)
            if (profileId != null) headers.add(HEADER_PROFILE_ID, profileId)
            if (roles != null) headers.add(ROLES, roles)
        }
    }

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
            if (status == HttpStatus.FORBIDDEN) {
                return webclientExchange_GET_403(webTestClient, endpoint, headers)
            }
        }

        if (httpMethod == HttpMethod.DELETE) {
            if (status == HttpStatus.OK) {
                return webclientExchange_DELETE_200(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.BAD_REQUEST) {
                return webclientExchange_DELETE_400(webTestClient, endpoint, headers)
            }
            if (status == HttpStatus.NOT_FOUND) {
                return webclientExchange_DELETE_404(webTestClient, endpoint, headers)
            }
        }

        if (httpMethod == HttpMethod.POST && requestBody != null) {
            if (status == HttpStatus.OK){
                return webclientExchange_POST_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.NOT_FOUND) {
                return webclientExchange_POST_WITH_REQUEST_BODY_404(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.BAD_REQUEST) {
                return webclientExchange_POST_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.FORBIDDEN) {
                return webclientExchange_POST_WITH_REQUEST_BODY_403(webTestClient, endpoint, requestBody, headers)
            }
        }

        if (httpMethod == HttpMethod.PATCH && requestBody != null) {
            if (status == HttpStatus.OK) {
                return webclientExchange_PATCH_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.BAD_REQUEST) {
                return webclientExchange_PATCH_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.NOT_FOUND) {
                return webclientExchange_PATCH_WITH_REQUEST_BODY_404(webTestClient, endpoint, requestBody, headers)
            }
        }

        if (httpMethod == HttpMethod.PUT && requestBody != null) {
            if (status == HttpStatus.OK) {
                return webclientExchange_PUT_WITH_REQUEST_BODY_200(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.BAD_REQUEST){
                return webclientExchange_PUT_WITH_REQUEST_BODY_400(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.FORBIDDEN){
                return webclientExchange_PUT_WITH_REQUEST_BODY_403(webTestClient, endpoint, requestBody, headers)
            }
            if (status == HttpStatus.NOT_FOUND){
                return webclientExchange_PUT_WITH_REQUEST_BODY_404(webTestClient, endpoint, requestBody, headers)
            }
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

    private static EntityExchangeResult<byte[]> webclientExchange_DELETE_200(WebTestClient webTestClient,
                                                                          String requestUrl,
                                                                          Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .delete()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isOk().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_DELETE_404(WebTestClient webTestClient,
                                                                             String requestUrl,
                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .delete()
                .uri(requestUrl)
                .headers(headersConsumer)
                .exchange()
                .expectStatus().isNotFound().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_DELETE_400(WebTestClient webTestClient,
                                                                             String requestUrl,
                                                                             Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .delete()
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

    private static EntityExchangeResult<byte[]> webclientExchange_PATCH_WITH_REQUEST_BODY_404(WebTestClient webTestClient,
                                                                                              String requestUrl,
                                                                                              Object requestBody,
                                                                                              Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .patch()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isNotFound().expectBody().returnResult()
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

    private static EntityExchangeResult<byte[]> webclientExchange_PUT_WITH_REQUEST_BODY_403(WebTestClient webTestClient,
                                                                                            String requestUrl,
                                                                                            Object requestBody,
                                                                                            Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .put()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isForbidden().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_PUT_WITH_REQUEST_BODY_404(WebTestClient webTestClient,
                                                                                            String requestUrl,
                                                                                            Object requestBody,
                                                                                            Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .put()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isNotFound().expectBody().returnResult()
    }

    private static EntityExchangeResult<byte[]> webclientExchange_POST_WITH_REQUEST_BODY_403(WebTestClient webTestClient,
                                                                                            String requestUrl,
                                                                                            Object requestBody,
                                                                                            Consumer<HttpHeaders> headersConsumer) {
        return webTestClient
                .post()
                .uri(requestUrl)
                .headers(headersConsumer)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus().isForbidden().expectBody().returnResult()
    }
}
