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
public class MockControllerWithParts_400 implements ApplicationConstants, Constants {

    @Owner
    @PostMapping(ENDPOINT_35)
    public ResponseEntity<Mono<String>> getSomething_35(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestPart(FILE) Mono<FilePart> file) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @PostMapping(ENDPOINT_36)
    public ResponseEntity<Mono<String>> getSomething_36(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestPart(FILE) Mono<FilePart> file,
                                                        @RequestPart(value = FIELD, required = false) String field) {
        return ResponseEntity.ok(Mono.empty());
    }

}