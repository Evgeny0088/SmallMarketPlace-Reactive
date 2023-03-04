package com.marketplace.logger.controller;

import com.marketplace.logger.common.ApplicationConstants;
import com.marketplace.logger.dto.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.marketplace.logger.common.ApplicationConstants.API_PREFIX;

@RestController
@RequestMapping(path = API_PREFIX, method = RequestMethod.DELETE)
@Slf4j
public class Controller_PathPrefix_Path_200 implements ApplicationConstants {

    @GetMapping(ENDPOINT_20)
    public ResponseEntity<Mono<TestResponse<String>>> loggerTest_20(@PathVariable("var") String var,
                                                                    @RequestHeader(value = EXTRA_HEADER, required = false) String header) {
        TestResponse<String> testResponse = new TestResponse<>();
        testResponse.setResponseBody("simple response body...");
        return ResponseEntity.ok(Mono.just(testResponse));
    }
}
