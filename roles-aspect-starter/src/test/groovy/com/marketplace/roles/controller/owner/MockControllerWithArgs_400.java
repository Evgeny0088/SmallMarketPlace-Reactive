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
public class MockControllerWithArgs_400 implements ApplicationConstants, Constants {

    @Owner
    @GetMapping(ENDPOINT_20)
    public ResponseEntity<Mono<String>> getSomething_20(@RequestHeader(PROFILE_ID) String ownerId) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_21)
    public ResponseEntity<Mono<String>> getSomething_21(@RequestParam(value = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_22)
    public ResponseEntity<Mono<String>> getSomething_22() {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_23)
    public ResponseEntity<Mono<String>> getSomething_23(@PathVariable("profileId") String profileId,
                                                        @RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(value = "one", required = false) long param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_24)
    public ResponseEntity<Mono<String>> getSomething_24(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_25)
    public ResponseEntity<Mono<String>> getSomething_25(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestParam(value = "two", required = false) long two,
                                                        @RequestParam(value = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_26)
    public ResponseEntity<Mono<String>> getSomething_26(@RequestHeader(PROFILE_ID) String wrongId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestParam(value = "two", required = false) long two,
                                                        @RequestParam(value = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }
}
