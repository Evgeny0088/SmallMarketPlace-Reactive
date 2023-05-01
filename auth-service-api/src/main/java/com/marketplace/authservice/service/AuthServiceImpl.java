package com.marketplace.authservice.service;

import com.marketplace.ProfileDto;
import com.marketplace.authservice.common.Constants;
import com.marketplace.authservice.dto.ResponseDto;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.jwt.dto.JwtDto;
import com.marketplace.jwt.jwtservice.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService, Constants {

    private final WebClient profileClient;
    private final JWTService jwtService;

    public AuthServiceImpl(WebClient profileClient, JWTService jwtService) {
        this.profileClient = profileClient;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<ResponseDto<ProfileDto>> registration(ProfileDto profileDto) {
        return profileClient
                .method(HttpMethod.POST)
                .uri(String.format(SAVE_PROFILE_PATH, COMMON_PREFIX_PROFILE))
                .body(BodyInserters.fromValue(profileDto))
                .retrieve()
                .bodyToMono(ProfileDto.class)
                .log()
                .map((profile)-> ResponseDto.<ProfileDto>builder()
                        .message(REGISTRATION_MESSAGE_RESPONSE_200)
                        .content(profile)
                        .build())
                .log()
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2))
                        .filter((throwable -> throwable instanceof WebClientRequestException))
                        .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> new CustomException(HttpStatus.SERVICE_UNAVAILABLE))))
                .onErrorMap(Exceptions::propagate);
    }

    @Override
    public Mono<ResponseDto<JwtDto>> login(ProfileDto profileDto) {
        return profileClient
                .method(HttpMethod.POST)
                .uri(String.format(VERIFY_PROFILE_PATH, COMMON_PREFIX_PROFILE))
                .body(BodyInserters.fromValue(profileDto))
                .retrieve()
                .bodyToMono(ProfileDto.class)
                .map((verifiedProfile) -> ResponseDto.<JwtDto>builder()
                        .message(ACCESS_TOKEN_IS_PROVIDED)
                        .content(jwtService.createToken(verifiedProfile))
                        .build())
                .onErrorMap(Exceptions::propagate);
    }

    @Override
    public Mono<ResponseDto<String>> confirmProfileChanges(String profileId) {
        return profileClient
                .method(HttpMethod.GET)
                .uri(String.format(CONFIRM_PROFILE_CHANGES, COMMON_PREFIX_PROFILE, profileId))
                .body(BodyInserters.fromValue(profileId))
                .retrieve()
                .bodyToMono(ResponseDto.class)
                .map((response) -> ResponseDto.<String>builder()
                        .message(response.getMessage())
                        .build())
                .onErrorMap(Exceptions::propagate);
    }

    @Override
    public Mono<ResponseDto<String>> requestEmail(ProfileDto profileDto) {
        return profileClient
                .method(HttpMethod.POST)
                .uri(String.format(REQUEST_EMAIL_AGAIN_PATH, COMMON_PREFIX_PROFILE))
                .body(BodyInserters.fromValue(profileDto))
                .retrieve()
                .bodyToMono(String.class)
                .map((response)-> ResponseDto.<String>builder()
                        .message(response)
                        .build())
                .onErrorMap(Exceptions::propagate);
    }
}
