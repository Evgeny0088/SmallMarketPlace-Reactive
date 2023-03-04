package com.marketplace.authservice.controller;

import com.marketplace.ProfileDto;
import com.marketplace.authservice.common.Constants;
import com.marketplace.authservice.dto.ResponseDto;
import com.marketplace.authservice.service.AuthService;
import com.marketplace.jwt.dto.JwtDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.marketplace.authservice.common.Constants.COMMON_PATH_PREFIX;

@RestController
@RequestMapping(COMMON_PATH_PREFIX)
@RequiredArgsConstructor
@Validated
public class AuthController implements Constants {

    private final AuthService authService;

    @PostMapping(REGISTRATION_PATH)
    public ResponseEntity<Mono<ResponseDto<ProfileDto>>> registration(@Valid @RequestBody ProfileDto profileDto) {
        return ResponseEntity.ok(authService.registration(profileDto));
    }

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<Mono<ResponseDto<JwtDto>>> login(@Valid @RequestBody ProfileDto profileDto) {
        return ResponseEntity.ok((authService.login(profileDto)));
    }

    @PostMapping(REQUEST_EMAIL_AGAIN)
    public ResponseEntity<Mono<ResponseDto<String>>> requestEmail(@RequestBody ProfileDto profileDto) {
        return ResponseEntity.ok(authService.requestEmail(profileDto));
    }

    @GetMapping(CONFIRM_PROFILE_CHANGES_PATH)
    public ResponseEntity<Mono<ResponseDto<String>>> confirmProfileChanges(@PathVariable(PROFILE_ID_VAR) String profileId) {
        return ResponseEntity.ok(authService.confirmProfileChanges(profileId));
    }
}