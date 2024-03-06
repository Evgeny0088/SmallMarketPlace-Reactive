package com.marketplace.sale.service.api.service;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.sale.service.api.common.Constants;
import com.marketplace.sale.service.api.dto.ItemTransactionRequest;
import com.marketplace.sale.service.api.dto.ItemTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoteCallsService implements Constants {

    private final WebClient itemsClient;

    public Mono<ItemTransactionResponse> requestForSale(ItemTransactionRequest request) {
        return itemsClient
                .method(HttpMethod.POST)
                .uri(ITEMS_PATH_SELL_REQUEST)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ItemTransactionResponse.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2))
                        .filter((throwable -> throwable instanceof WebClientRequestException))
                        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new CustomException(HttpStatus.SERVICE_UNAVAILABLE))))
                .onErrorMap(Exceptions::propagate)
                .log();
    }
}
