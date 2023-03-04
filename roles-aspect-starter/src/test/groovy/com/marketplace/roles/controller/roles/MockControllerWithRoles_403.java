package com.marketplace.roles.controller.roles;

import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.aspect.annotations.RolesRequired;
import com.marketplace.roles.common.ApplicationConstants;
import com.marketplace.roles.common.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MockControllerWithRoles_403 implements ApplicationConstants, Constants {

    @Owner
    @RolesRequired(roles = {MANAGER_ROLE, ADMIN_ROLE})
    @GetMapping(ENDPOINT_47)
    public ResponseEntity<Mono<String>> getSomething_47(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestHeader(ROLES) String roles) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {USER_ROLE, MANAGER_ROLE})
    @GetMapping(ENDPOINT_48)
    public ResponseEntity<Mono<String>> getSomething_48(@RequestHeader(ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {WRONG_ROLE})
    @GetMapping(ENDPOINT_49)
    public ResponseEntity<Mono<String>> getSomething_49(@RequestHeader(value = ROLES, required = false) String roles,
                                                        @PathVariable("profileId") String profileId) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {USER_ROLE, MANAGER_ROLE, WRONG_ROLE})
    @GetMapping(ENDPOINT_50)
    public ResponseEntity<Mono<String>> getSomething_50(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestHeader(ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestParam(value = "two", required = false) String two,
                                                        @RequestParam(value = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {ADMIN_ROLE})
    @GetMapping(ENDPOINT_51)
    public ResponseEntity<Mono<String>> getSomething_51(@RequestHeader(PROFILE_ID) String wrongId,
                                                        @PathVariable("profileId") String profileId,
                                                        @RequestHeader(ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestParam(value = "two", required = false) String two,
                                                        @RequestParam(value = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {WRONG_ROLE})
    @GetMapping(ENDPOINT_52)
    public ResponseEntity<Mono<String>> getSomething_52(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestHeader(ROLES) String roles) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {MANAGER_ROLE, ADMIN_ROLE})
    @GetMapping(ENDPOINT_53)
    public ResponseEntity<Mono<String>> getSomething_53(@RequestHeader(ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String one,
                                                        @RequestParam(value = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }
}
