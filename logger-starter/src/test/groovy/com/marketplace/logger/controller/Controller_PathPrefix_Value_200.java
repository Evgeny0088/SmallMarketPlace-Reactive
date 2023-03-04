package com.marketplace.logger.controller;

import com.marketplace.logger.common.ApplicationConstants;
import com.marketplace.logger.dto.DummyDto;
import com.marketplace.logger.dto.TestResponse;
import com.marketplace.logger.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.marketplace.logger.common.ApplicationConstants.API_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {API_PREFIX}, method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH})
@Slf4j
public class Controller_PathPrefix_Value_200 implements ApplicationConstants {

    private final TestService testService;

    @DeleteMapping(ENDPOINT_21)
    public ResponseEntity<Mono<TestResponse<String>>> loggerTest_21(@PathVariable("var") String var,
                                                                    @RequestParam(value = "one", required = false) String arg,
                                                                    @RequestHeader(value = EXTRA_HEADER, required = false) String header) {
        TestResponse<String> testResponse = new TestResponse<>();
        testResponse.setResponseBody("simple response body...");
        return ResponseEntity.ok(Mono.just(testResponse));
    }

    @RequestMapping(path = ENDPOINT_22, method = RequestMethod.POST)
    public Mono<Map<String, DummyDto>> loggerTest_22(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     ServerHttpRequest request,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @RequestMapping(name = "experimental", path = ENDPOINT_23, method = {RequestMethod.POST, RequestMethod.PUT})
    public Mono<Map<String, DummyDto>> loggerTest_23(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     @RequestPart(FILE) InputStreamResource resource,
                                                     @RequestPart(FIELD) String field,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @RequestMapping(name = "experimental", path = ENDPOINT_24, method = {RequestMethod.POST, RequestMethod.PUT})
    public Void loggerTest_24(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     @RequestPart(FILE) InputStreamResource resource,
                                                     @RequestPart(FIELD) String field,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        log.warn("just a void method");
        return null;
    }

    @RequestMapping(name = "experimental", path = ENDPOINT_25, method = {RequestMethod.POST, RequestMethod.PUT})
    public void loggerTest_25(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                              @RequestPart(FILE) InputStreamResource resource,
                              @RequestPart(FIELD) String field,
                              @RequestParam(value = "one", required = false) String param,
                              @RequestParam(value = "two", required = false) String two) {
        log.warn("just a void method without return");
    }

    @GetMapping(ENDPOINT_26)
    public ResponseEntity<Mono<DummyDto>> loggerTest_33(@RequestHeader(EXTRA_HEADER) String ownerId) {
        return ResponseEntity.ok(testService.returnTestData());
    }
}
