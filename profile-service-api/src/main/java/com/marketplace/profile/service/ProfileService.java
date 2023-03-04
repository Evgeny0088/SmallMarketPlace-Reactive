package com.marketplace.profile.service;

import com.marketplace.ProfileDto;
import com.marketplace.profile.dto.PageableRequest;
import com.marketplace.profile.dto.PageableResponse;
import com.marketplace.profile.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface ProfileService {

    Mono<ProfileDto> saveProfile(ProfileDto user);

    Mono<ProfileDto> verifyProfile(ProfileDto profileDto);

    Mono<ResponseDto<String>> updatedProfile(ProfileDto updatedUser);

    Mono<ResponseDto<String>> confirmProfileChanges(String profileId);

    Mono<String> requestEmailAgain(ProfileDto profileDto);

    Mono<ResponseDto<String>> unblockProfile(String profileId);

    Mono<ResponseDto<String>> blockProfile(String profileId);

    Mono<ResponseDto<PageableResponse>> getAllUnblocked(PageableRequest pageableRequest);

    Mono<ResponseDto<PageableResponse>> getAllBlocked(PageableRequest pageableRequest);

    Mono<ResponseDto<String>> deleteProfile(String profileId);

}
