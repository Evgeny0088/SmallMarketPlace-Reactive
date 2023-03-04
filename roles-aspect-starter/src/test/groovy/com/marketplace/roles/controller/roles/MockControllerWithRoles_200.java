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
public class MockControllerWithRoles_200 implements ApplicationConstants, Constants {

    @RolesRequired
    @GetMapping(ENDPOINT_37)
    public ResponseEntity<Mono<String>> getSomething_37(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(ROLES) String roles,
                                                        @PathVariable("profileId") String profileId) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired
    @GetMapping(ENDPOINT_38)
    public ResponseEntity<Mono<String>> getSomething_38(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestHeader(value = ROLES, required = false) String roles,
                                                       @RequestParam(name = "one", required = false) long param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {MANAGER_ROLE})
    @GetMapping(ENDPOINT_39)
    public ResponseEntity<Mono<String>> getSomething_39(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestHeader(value = ROLES, required = false) String roles,
                                                       @RequestParam(name = "one", required = false) boolean param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {MANAGER_ROLE})
    @GetMapping(ENDPOINT_40)
    public ResponseEntity<Mono<String>> getSomething_40(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @PathVariable("profileId") String profileId,
                                                       @RequestHeader(value = ROLES, required = false) String roles,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two,
                                                       @RequestParam(name = "three", required = false) String three) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {ADMIN_ROLE})
    @GetMapping(ENDPOINT_41)
    public ResponseEntity<Mono<String>> getSomething_41(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestHeader(value = ROLES) String roles,
                                                       @RequestParam(name = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {ADMIN_ROLE})
    @GetMapping(ENDPOINT_42)
    public ResponseEntity<Mono<String>> getSomething_42(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestHeader(value = ROLES) String roles,
                                                       @RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired(roles = {ADMIN_ROLE, MANAGER_ROLE})
    @GetMapping(ENDPOINT_43)
    public ResponseEntity<Mono<String>> getSomething_43(@RequestHeader(PROFILE_ID) String ownerId,
                                                       @RequestHeader(value = EXTRA_HEADER, required = false) String extra,
                                                       @RequestHeader(value = ROLES) String roles,
                                                       @PathVariable("profileId") String profileId,
                                                       @PathVariable("any") String any,
                                                       @RequestParam(name = "one", required = false) String one,
                                                       @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }

    @RolesRequired(roles = {MANAGER_ROLE, ADMIN_ROLE})
    @GetMapping(ENDPOINT_44)
    public ResponseEntity<Mono<String>> getSomething_44(@PathVariable("profileId") String profileId,
                                                        @RequestHeader(value = ROLES) String roles,
                                                        @PathVariable("any") String any,
                                                        @RequestParam(name = "one", required = false) String one,
                                                        @RequestParam(name = "two", required = false) String two) {
        return ResponseEntity.ok(Mono.empty());
    }


    @Owner
    @RolesRequired(roles = {MANAGER_ROLE})
    @GetMapping(ENDPOINT_45)
    public ResponseEntity<Mono<String>> getSomething_45(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(value = ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

    @Owner
    @RolesRequired
    @GetMapping(ENDPOINT_46)
    public ResponseEntity<Mono<String>> getSomething_46(@RequestHeader(PROFILE_ID) String ownerId,
                                                        @RequestHeader(ROLES) String roles,
                                                        @RequestParam(value = "one", required = false) String param) {
        return ResponseEntity.ok(Mono.empty());
    }

}