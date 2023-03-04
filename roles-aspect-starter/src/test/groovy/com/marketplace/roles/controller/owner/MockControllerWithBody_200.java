package com.marketplace.roles.controller.owner;

import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.common.ApplicationConstants;
import com.marketplace.roles.common.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MockControllerWithBody_200 implements ApplicationConstants, Constants {

    @Owner
    @PostMapping(ENDPOINT_9)
    public ResponseEntity<Mono<String>> getSomething_9(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_10)
    public ResponseEntity<Mono<String>> getSomething_10(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(name = "one", required = false) String param,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_11)
    public ResponseEntity<Mono<String>> getSomething_11(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(EXTRA_HEADER) String extra,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_12)
    public ResponseEntity<Mono<String>> getSomething_12(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestHeader(EXTRA_HEADER) String extra,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_13)
    public ResponseEntity<Mono<String>> getSomething_13(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_14)
    public ResponseEntity<Mono<String>> getSomething_14(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(name = "one", required = false) String param,
                                                        @RequestHeader(EXTRA_HEADER) String extra,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PatchMapping(ENDPOINT_15)
    public ResponseEntity<Mono<String>> getSomething_15(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PutMapping(ENDPOINT_16)
    public ResponseEntity<Mono<String>> getSomething_16(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(name = "one", required = false) String one,
                                                        @RequestParam(name = "two", required = false) String two,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

}