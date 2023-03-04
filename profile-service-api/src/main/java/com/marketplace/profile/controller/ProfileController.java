package com.marketplace.profile.controller;

import com.marketplace.ProfileDto;
import com.marketplace.profile.common.Constants;
import com.marketplace.profile.common.MessageConstants;
import com.marketplace.profile.dto.PageableRequest;
import com.marketplace.profile.dto.PageableResponse;
import com.marketplace.profile.dto.ResponseDto;
import com.marketplace.profile.service.ProfileService;
import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.aspect.annotations.RolesRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.marketplace.profile.common.Constants.COMMON_PATH_PREFIX;

@RestController
@RequestMapping(COMMON_PATH_PREFIX)
@RequiredArgsConstructor
@Validated
public class ProfileController implements Constants, MessageConstants {

    private final ProfileService profileService;

    @PostMapping(SAVE_PROFILE_PATH)
    public Mono<ProfileDto> saveNewProfile(@Valid @RequestBody ProfileDto profileDto){
        return profileService.saveProfile(profileDto);
    }

    @PostMapping(VERIFY_PROFILE_PATH)
    public Mono<ProfileDto> verifyProfileForAuthentication(@Valid @RequestBody ProfileDto profileDto){
        return profileService.verifyProfile(profileDto);
    }

    @PostMapping(REQUEST_EMAIL_AGAIN)
    public Mono<String> requestEmail(@RequestBody ProfileDto profileDto) {
        return profileService.requestEmailAgain(profileDto);
    }

    @Owner
    @RolesRequired(roles = "ADMIN")
    @PatchMapping(UPDATE_PROFILE_PATH)
    public Mono<ResponseDto<String>> updateProfile(@Valid @NotBlank @RequestHeader(HEADER_PROFILE_ID) String ownerId,
                                                   @RequestHeader(value = ROLES, required = false) String roles,
                                                   @RequestBody ProfileDto updatedProfile) {
        return profileService.updatedProfile(updatedProfile);
    }

    @GetMapping(CONFIRM_PROFILE_CHANGES_PATH)
    public Mono<ResponseDto<String>> confirmProfileChanges(@Valid @PathVariable(PROFILE_ID_VAR) String profileId) {
        return profileService.confirmProfileChanges(profileId);
    }

    @RolesRequired(roles = "ADMIN")
    @GetMapping(BLOCK_PROFILE_PATH)
    public Mono<ResponseDto<String>> blockProfile(@Valid @PathVariable(PROFILE_ID_VAR) String profileId,
                                                  @RequestHeader(ROLES) String roles) {
        return profileService.blockProfile(profileId);
    }

    @RolesRequired(roles = {"ADMIN"})
    @GetMapping(UNBLOCK_PROFILE_PATH)
    public Mono<ResponseDto<String>> unblockProfile(@Valid @PathVariable(PROFILE_ID_VAR) String profileId,
                                                    @RequestHeader(ROLES) String roles) {
        return profileService.unblockProfile(profileId);
    }

    @Owner
    @RolesRequired(roles = {"ADMIN"})
    @DeleteMapping(DELETE_PROFILE)
    public Mono<ResponseDto<String>> deleteProfile(@Valid @NotBlank @RequestHeader(HEADER_PROFILE_ID) String ownerId,
                                                   @RequestHeader(value = ROLES, required = false) String roles,
                                                   @Valid @PathVariable(PROFILE_ID_VAR) String profileId) {
        return profileService.deleteProfile(profileId);
    }

    @RolesRequired(roles = {"ADMIN", "MANAGER"})
    @GetMapping(GET_ALL_UNBLOCKED_PROFILES_PATH)
    public ResponseEntity<Mono<ResponseDto<PageableResponse>>> getAllUnblockedProfiles(@RequestHeader(ROLES) String roles,
                                                                                       PageableRequest pageableRequest) {
        return ResponseEntity.ok(profileService.getAllUnblocked(pageableRequest));
    }

    @RolesRequired(roles = {"ADMIN", "MANAGER"})
    @GetMapping(GET_ALL_BLOCKED_PROFILES_PATH)
    public ResponseEntity<Mono<ResponseDto<PageableResponse>>> getAllBlockedProfiles(@RequestHeader(ROLES) String roles,
                                                                                     PageableRequest pageableRequest) {
        return ResponseEntity.ok(profileService.getAllBlocked(pageableRequest));
    }
}