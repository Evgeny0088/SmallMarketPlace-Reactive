package com.marketplace.error.handler.reactive.webclient;

import com.marketplace.error.handler.reactive.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientErrorHandler implements Constants {

    @Bean("webFilter")
    public ExchangeFilterFunction exchangeFilterFunction() {
        return ((request, next) -> next.exchange(request)
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()
                            || clientResponse.statusCode().is3xxRedirection()) {
                        return Mono.just(clientResponse);
                    }
                    return clientResponse.createException()
                            .flatMap(Mono::error);
                }));
    }
}