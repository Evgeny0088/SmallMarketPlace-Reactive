package com.marketplace.logger.controller;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.logger.common.ApplicationConstants;
import com.marketplace.logger.dto.DummyDto;
import com.marketplace.logger.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class LoggerTestController_400 implements ApplicationConstants {

    private final TestService testService;

    @GetMapping(ENDPOINT_30)
    public ResponseEntity<Mono<String>> loggerTest_30(@RequestHeader(EXTRA_HEADER) String ownerId) {
        throw new CustomException(HttpStatus.BAD_REQUEST).setDetails("this is error");
    }

    @GetMapping(ENDPOINT_31)
    public ResponseEntity<Mono<String>> loggerTest_31(@RequestHeader(EXTRA_HEADER) String ownerId) {
        throw new RuntimeException("this is error");
    }

    @GetMapping(ENDPOINT_32)
    public ResponseEntity<Mono<Object>> loggerTest_32(@RequestHeader(EXTRA_HEADER) String ownerId) {
        return ResponseEntity.ok(testService.returnTestError());
    }

}
