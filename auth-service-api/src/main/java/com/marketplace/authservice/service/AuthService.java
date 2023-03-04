package com.marketplace.authservice.service;

import com.marketplace.ProfileDto;
import com.marketplace.authservice.dto.ResponseDto;
import com.marketplace.jwt.dto.JwtDto;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<ResponseDto<ProfileDto>> registration(ProfileDto user);

    Mono<ResponseDto<JwtDto>> login(ProfileDto updatedProfileDto);

    Mono<ResponseDto<String>> confirmProfileChanges(String profileId);

    Mono<ResponseDto<String>> requestEmail(ProfileDto profileDto);

}
