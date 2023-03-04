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
public class MockControllerWithBody_400 implements ApplicationConstants, Constants {

    @Owner
    @PostMapping(ENDPOINT_27)
    public ResponseEntity<Mono<String>> getSomething_27(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_28)
    public ResponseEntity<Mono<String>> getSomething_28(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(name = "one", required = false) String param,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_29)
    public ResponseEntity<Mono<String>> getSomething_29(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(name = "one", required = false) String param,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_30)
    public ResponseEntity<Mono<String>> getSomething_30(@RequestParam(name = "one", required = false) String param,
                                                        @RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_31)
    public ResponseEntity<Mono<String>> getSomething_31(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_32)
    public ResponseEntity<Mono<String>> getSomething_32(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_33)
    public ResponseEntity<Mono<String>> getSomething_33(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_34)
    public ResponseEntity<Mono<String>> getSomething_34(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestBody Object requestBody) {
        return ResponseEntity.ok(Mono.empty());
    }
}
