package com.marketplace.logger.service;

import com.marketplace.logger.dto.DummyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TestService {

    public Mono<Object> returnTestError() {
        return Mono.just(new DummyDto())
                .map((dummyDto -> {
                    dummyDto.setEmail("new email");
                    throw new RuntimeException();
                }))
                .onErrorMap(Exceptions::propagate);
    }

    public Mono<DummyDto> returnTestData() {
        return Mono.just(new DummyDto())
                .map((dummyDto -> {
                    dummyDto.setEmail("new email");
                    return dummyDto;
                }))
                .onErrorMap(Exceptions::propagate);
    }
}
