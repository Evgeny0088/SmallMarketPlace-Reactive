package com.marketplace.logger.controller;

import com.marketplace.logger.common.ApplicationConstants;
import com.marketplace.logger.dto.DummyDto;
import com.marketplace.logger.dto.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.marketplace.logger.utils.HelpTestMethods.generateDummyDto;

@RestController
@Slf4j
public class LoggerTestController_200 implements ApplicationConstants {

    @GetMapping(ENDPOINT_1)
    public ResponseEntity<Mono<TestResponse<String>>> loggerTest_1(@PathVariable("var") String var) {
        TestResponse<String> testResponse = new TestResponse<>();
        testResponse.setResponseBody("simple response body...");
        return ResponseEntity.ok(Mono.just(testResponse));
    }

    @GetMapping(name = "any name of endpoint", path = ENDPOINT_2, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Mono<TestResponse<DummyDto>>> loggerTest_2(@PathVariable("var") String var,
                                                                     @RequestParam(value = "one", required = false) int one) {
        TestResponse<DummyDto> testResponse = new TestResponse<>();
        testResponse.setResponseBody(new DummyDto());
        return ResponseEntity.ok(Mono.just(testResponse));
    }

    @PostMapping(value = ENDPOINT_3)
    public ResponseEntity<Flux<Integer>> loggerTest_3(@PathVariable("var") String var,
                                                      @RequestBody Object requestBody,
                                                      @RequestParam(value = "one", required = false) String one) {
        return ResponseEntity.ok(Flux.just(IntStream.rangeClosed(0, 100).boxed().toList().toArray(new Integer[0])));
    }

    @PostMapping(ENDPOINT_4)
    public ResponseEntity<Mono<String>> loggerTest_4(@PathVariable("var") String var,
                                                     @RequestBody List<String> requestBody,
                                                     @RequestParam(value = "one", required = false) boolean one){
        return ResponseEntity.ok(Mono.empty());
    }

    @PostMapping(ENDPOINT_5)
    public ResponseEntity<Flux<String>> loggerTest_5(@PathVariable("var") long var,
                                                     @RequestBody List<Object> requestBody) {
        return ResponseEntity.ok(Flux.empty());
    }

    @PutMapping(ENDPOINT_6)
    public ResponseEntity<Flux<TestResponse<DummyDto>>> loggerTest_6(@PathVariable("arg1") long arg1,
                                                                     @PathVariable("arg2") String arg2,
                                                                     @RequestHeader(EXTRA_HEADER) String header,
                                                                     @RequestBody Object requestBody) {
        TestResponse<DummyDto> testResponse = new TestResponse<>();
        testResponse.setResponseBody(new DummyDto());
        return ResponseEntity.ok(Flux.from(Mono.just(testResponse)));
    }

    @PatchMapping(ENDPOINT_7)
    public ResponseEntity<Flux<DummyDto>> loggerTest_7(@RequestParam("one") String one,
                                                       @RequestParam("two") int two,
                                                       @RequestParam("three") boolean three,
                                                       @RequestHeader(EXTRA_HEADER) String header,
                                                       @RequestBody Object requestBody) {
        return ResponseEntity.ok(Flux.fromIterable(generateDummyDto(10)));
    }

    @DeleteMapping(ENDPOINT_8)
    public ResponseEntity<Flux<TestResponse<List<DummyDto>>>> loggerTest_8(@RequestParam("one") String one,
                                                                           @RequestParam("two") int two,
                                                                           @PathVariable("arg1") String arg1,
                                                                           @RequestParam("three") boolean three,
                                                                           @RequestHeader(EXTRA_HEADER) String header) {
        TestResponse<List<DummyDto>> testResponse = new TestResponse<>();
        testResponse.setResponseBody(generateDummyDto(10));
        return ResponseEntity.ok(Flux.just(testResponse));
    }

    @GetMapping(ENDPOINT_9)
    public ResponseEntity<Void> loggerTest_9(@RequestParam("one") String one,
                                             @RequestParam("two") int two,
                                             @PathVariable("arg1") String arg1,
                                             @RequestParam("three") boolean three,
                                             @RequestHeader(EXTRA_HEADER) String header) {
        return ResponseEntity.ok().build();
    }

    @PostMapping(ENDPOINT_10)
    public ResponseEntity<Mono<Map<String, DummyDto>>> loggerTest_10(@RequestHeader(EXTRA_HEADER) String extra,
                                                                     @RequestParam(value = "one", required = false) String param,
                                                                     @RequestPart(FILE) Mono<FilePart> file,
                                                                     @RequestPart(value = FIELD, required = false) Mono<String> field) {
        return ResponseEntity.ok(Mono.just(Map.of("This is just message", new DummyDto())));
    }

    @PostMapping(ENDPOINT_11)
    public ResponseEntity<Mono<ServerResponse>> loggerTest_11(@RequestHeader(EXTRA_HEADER) String extra,
                                                             @RequestParam(value = "one", required = false) String param,
                                                             @RequestPart(FILE) Mono<FilePart> file,
                                                             @RequestPart(value = FIELD, required = false) String field) {

        Map<String, Object> responseBodyMap = new HashMap<>();
        responseBodyMap.put("httpCode", 200);
        responseBodyMap.put("path", ENDPOINT_11);
        responseBodyMap.put("response body", new DummyDto());

        return ResponseEntity.ok(ServerResponse
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBodyMap)));
    }

    @PostMapping(ENDPOINT_12)
    public Mono<ServerResponse> loggerTest_12(@RequestHeader(EXTRA_HEADER) String extra,
                                              @RequestParam(value = "one", required = false) String param,
                                              @RequestPart(FILE) Mono<FilePart> file,
                                              @RequestPart(value = FIELD, required = false) String field) {

        Map<String, Object> responseBodyMap = new HashMap<>();
        responseBodyMap.put("httpCode", 200);
        responseBodyMap.put("path", ENDPOINT_11);
        responseBodyMap.put("response body", new DummyDto());

        return ServerResponse
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBodyMap));
    }

    @PostMapping(path = {ENDPOINT_13})
    public ResponseEntity<Flux<Map<String, DummyDto>>> loggerTest_13(@RequestParam(value = "one", required = false) String param,
                                                                     @RequestPart(value = FILE, required = false) Mono<FilePart> file,
                                                                     @RequestParam("three") boolean three) {
        return ResponseEntity.ok(Flux.just(Map.of("This is just message", new DummyDto())));
    }

    @PostMapping(path = ENDPOINT_14)
    public Mono<Map<String, DummyDto>> loggerTest_14(@RequestParam(value = "one", required = false) String param,
                                                     @RequestParam("two") String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @PostMapping(value = ENDPOINT_15)
    public Mono<Map<String, DummyDto>> loggerTest_15(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @PostMapping(path = ENDPOINT_16)
    public Flux<Mono<ServerResponse>> loggerTest_16(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                    @RequestParam(value = "one", required = false) String param,
                                                    @RequestParam(value = "two", required = false) String two) {
        Map<String, Object> responseBodyMap = new HashMap<>();
        responseBodyMap.put("httpCode", 200);
        responseBodyMap.put("path", ENDPOINT_11);
        responseBodyMap.put("response body", new DummyDto());

        return Flux.just(ServerResponse
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBodyMap)));
    }

    @PostMapping(path = ENDPOINT_17)
    public Mono<Map<String, DummyDto>> loggerTest_17(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     ServerHttpRequest request,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @PostMapping(path = ENDPOINT_18)
    public Mono<Map<String, DummyDto>> loggerTest_18(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     @RequestPart(FILE) InputStreamResource resource,
                                                     @RequestPart(FIELD) String field,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }

    @RequestMapping(name = "experimental", path = ENDPOINT_19, method = {RequestMethod.GET}, consumes = MediaType.ALL_VALUE)
    public Mono<Map<String, DummyDto>> loggerTest_19(@RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                     @RequestParam(value = "one", required = false) String param,
                                                     @RequestParam(value = "two", required = false) String two) {
        return Mono.just(Map.of("This is just message", new DummyDto()));
    }
}
