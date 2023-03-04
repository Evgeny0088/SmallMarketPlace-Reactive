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
public class MockControllerWithArgs_200 implements ApplicationConstants, Constants {

    @Owner
    @GetMapping(ENDPOINT_1)
    public ResponseEntity<Mono<String>> getSomething_1(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_2)
    public ResponseEntity<Mono<String>> getSomething_2(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestParam(name = "one", required = false) long param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_3)
    public ResponseEntity<Mono<String>> getSomething_3(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestParam(name = "one", required = false) boolean param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_4)
    public ResponseEntity<Mono<String>> getSomething_4(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two,
                                                       @RequestParam(name = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_5)
    public ResponseEntity<Mono<String>> getSomething_5(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestParam(name = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_6)
    public ResponseEntity<Mono<String>> getSomething_6(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_7)
    public ResponseEntity<Mono<String>> getSomething_7(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                       @PathVariable("profileId") String profileId,
                                                       @PathVariable("any") String any,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @GetMapping(ENDPOINT_8)
    public ResponseEntity<Mono<String>> getSomething_8(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @PathVariable("any") String any,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }
}