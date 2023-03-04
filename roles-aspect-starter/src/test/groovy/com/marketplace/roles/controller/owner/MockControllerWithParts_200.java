package com.marketplace.roles.controller.owner;

import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.common.ApplicationConstants;
import com.marketplace.roles.common.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MockControllerWithParts_200 implements ApplicationConstants, Constants {

    @Owner
    @PostMapping(ENDPOINT_17)
    public ResponseEntity<Mono<String>> getSomething_17(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestPart(FILE) Mono<FilePart> file) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_18)
    public ResponseEntity<Mono<String>> getSomething_18(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(EXTRA_HEADER) String extra,
                                                        @RequestParam(value = "one", required = false) String param,
                                                        @RequestPart(FILE) Mono<FilePart> file,
                                                        @RequestPart(value = FIELD, required = false) String field) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_19)
    public ResponseEntity<Mono<String>> getSomething_19(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(EXTRA_HEADER) String extra,
                                                        @RequestPart(FILE) Mono<FilePart> file,
                                                        @RequestPart(value = FIELD, required = false) String field) {
        return ResponseEntity.ok(Mono.empty());
    }

}