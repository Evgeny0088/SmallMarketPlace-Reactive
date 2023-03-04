package com.marketplace.test.helper.starter.controller;

import com.marketplace.test.helper.starter.TestConstants;
import com.marketplace.test.helper.starter.dto.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class TestController implements TestConstants {

    @GetMapping(ENDPOINT_1)
    public ResponseEntity<Mono<TestResponse<String>>> loggerTest_1(@PathVariable("var") String var,
                                                                    @RequestHeader(value = "extra-header", required = false) String header) {
        TestResponse<String> testResponse = new TestResponse<>();
        testResponse.setResponseBody("simple response body...");
        return ResponseEntity.ok(Mono.just(testResponse));
    }

    @GetMapping(ENDPOINT_2)
    public ResponseEntity<Mono<TestResponse<String>>> loggerTest_2(@PathVariable("var") String var) {
        TestResponse<String> testResponse = new TestResponse<>();
        testResponse.setResponseBody("simple response body...");
        return ResponseEntity.ok(Mono.just(testResponse));
    }
}
