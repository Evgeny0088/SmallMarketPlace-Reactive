package com.marketplace.profile.service;

import com.marketplace.ProfileDto;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.mail.sender.service.MailService;
import com.marketplace.profile.common.Constants;
import com.marketplace.profile.common.MessageConstants;
import com.marketplace.profile.dto.PageableRequest;
import com.marketplace.profile.dto.PageableResponse;
import com.marketplace.profile.dto.ResponseDto;
import com.marketplace.profile.entity.Profile;
import com.marketplace.profile.mapper.ProfileMapper;
import com.marketplace.profile.properties.ProfileUpdateProperties;
import com.marketplace.profile.properties.ServiceProperties;
import com.marketplace.profile.repository.ProfileRepo;
import com.marketplace.profile.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

import static com.marketplace.profile.utils.HelpServiceMethods.*;
import static com.marketplace.profile.utils.RepositoryUtils.getAllProfiles;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService, Constants, MessageConstants {

    private final ServiceProperties serviceProperties;
    private final ProfileRepo profileRepo;
    private final RoleRepo roleRepo;
    private final MailService mailService;
    private final ProfileMapper profileMapper;
    private final ProfileUpdateProperties profileUpdateProps;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public Mono<ProfileDto> saveProfile(ProfileDto profileDto) {
        Set<String> verifiedRoles = new HashSet<>();
        Profile profile = profileMapper.profileDtoToProfile(profileDto);
        return Mono.just(profile)
                .flatMap((p)-> Flux.just(profile.getRoles() != null && !profile.getRoles().isEmpty()
                                ? profile.getRoles().split(ROLE_DELIMITER)
                                : new String[0])
                        .log()
                        .filterWhen(roleRepo::existsByName).distinct()
                        .doOnNext(verifiedRoles::add)
                        .doOnComplete(()-> roleSetup(profile, verifiedRoles))
                        .then(profileRepo.save(profile))
                        .doOnSuccess((savedProfile)-> sendMailNotification(savedProfile, profileRepo, mailService,
                                String.format(MAIL_MESSAGE, savedProfile.getUsername(), serviceProperties.getName(), savedProfile.getId()),
                                profileUpdateProps, false))
                        .onErrorMap(Exceptions::propagate)
                )
                .flatMap((savedProfile)-> profileRepo
                        .findProfileByUsername(savedProfile.getUsername())
                        .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                                .setDetails(String.format(PROFILE_NOT_FOUND, profileDto.getUsername()))))
                        .map(profileMapper::profileToProfileDto
                )
                .onErrorMap(Exceptions::propagate)
                .log());
    }

    @Override
    public Mono<ProfileDto> verifyProfile(ProfileDto profileDto) {
        return profileRepo.findProfileByUsername(profileDto.getUsername())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileDto.getUsername()))))
                .<ProfileDto>handle((foundProfile, sink) -> {
                    if (!encoder.matches(profileDto.getPassword(), foundProfile.getPassword())) {
                        sink.error(new CustomException(HttpStatus.BAD_REQUEST)
                                .setDetails(PROFILE_PASSWORD_WRONG));
                        return;
                    }
                    if (!foundProfile.isActive()) {
                        sink.error(new CustomException(HttpStatus.BAD_REQUEST)
                                .setDetails(PROFILE_IS_NOT_ACTIVE));
                        return;
                    }
                    if (foundProfile.isBlocked()) {
                        sink.error(new CustomException(HttpStatus.BAD_REQUEST)
                                .setDetails(PROFILE_BLOCKED_INFO));
                        return;
                    }
                    sink.next(profileMapper.profileToProfileDto(foundProfile));
                })
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Transactional
    @Override
    public Mono<ResponseDto<String>> updatedProfile(ProfileDto updatedInputs) {
        Set<String> verifiedRoles = new HashSet<>();
        String profileId = updatedInputs.getId();
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .flatMap((foundProfile)->
                        Flux.just(updatedInputs.getRoles() != null && !updatedInputs.getRoles().isEmpty()
                                ? updatedInputs.getRoles().split(ROLE_DELIMITER)
                                : new String[0]
                                )
                        .filterWhen(roleRepo::existsByName).distinct()
                        .doOnNext(verifiedRoles::add)
                        .doOnComplete(() -> profileUpdateSetup(foundProfile, updatedInputs, verifiedRoles,
                                                                profileUpdateProps.getUpdateAfter(), encoder))
                        .then(profileRepo.save(foundProfile)))
                .log()
                .thenReturn(ResponseDto.<String>builder()
                        .message(PROFILE_UPDATE_MESSAGE)
                        .content(String.format(PROFILE_UPDATES_CONFIRMED, updatedInputs.getUsername()))
                        .build())
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Override
    public Mono<ResponseDto<String>> confirmProfileChanges(String profileId) {
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .flatMap((profile)-> setActive(profileRepo, profile, profileUpdateProps, true))
                .thenReturn(ResponseDto.<String>builder()
                        .message(String.format(PROFILE_UPDATES_CONFIRMED,profileId))
                        .build())
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Override
    public Mono<ResponseDto<String>> blockProfile(String profileId) {
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .flatMap((foundProfile)-> setBlockProfile(profileRepo, foundProfile))
                .thenReturn(ResponseDto.<String>builder()
                        .content(String.format(PROFILE_IS_BLOCKED, profileId))
                        .build())
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Override
    public Mono<ResponseDto<String>> unblockProfile(String profileId) {
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .flatMap((foundProfile)-> setUnBlockProfile(profileRepo, foundProfile))
                .thenReturn(ResponseDto.<String>builder()
                        .content(String.format(PROFILE_IS_UNBLOCKED, profileId))
                        .build())
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Override
    public Mono<ResponseDto<PageableResponse>> getAllUnblocked(PageableRequest pageableRequest) {
        return getAllProfiles(pageableRequest, profileUpdateProps, UPDATED_AT, profileRepo, false, UNBLOCKED_PROFILE_LIST);
    }

    @Override
    public Mono<ResponseDto<PageableResponse>> getAllBlocked(PageableRequest pageableRequest) {
        return getAllProfiles(pageableRequest, profileUpdateProps, UPDATED_AT, profileRepo, true, BLOCKED_PROFILE_LIST);
    }

    @Override
    public Mono<ResponseDto<String>> deleteProfile(String profileId) {
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .flatMap(profileRepo::delete)
                .thenReturn(ResponseDto.<String>builder()
                        .content(String.format(PROFILE_IS_DELETED, profileId))
                        .build())
                .onErrorMap(Exceptions::propagate)
                .log();
    }

    @Override
    public Mono<String> requestEmailAgain(ProfileDto profileDto) {
        String profileId = profileDto.getId();
        profileId = profileId != null && !profileId.isBlank() ? profileId : Strings.EMPTY;
        return profileRepo.findById(profileId)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)
                        .setDetails(String.format(PROFILE_NOT_FOUND, profileId))))
                .doOnSuccess((savedProfile)-> sendMailNotification(savedProfile, profileRepo, mailService,
                        String.format(MAIL_MESSAGE, savedProfile.getUsername(), serviceProperties.getName(), savedProfile.getId()),
                        profileUpdateProps, true)
                )
                .then(Mono.just(CONFIRMATION_EMAIL_MESSAGE))
                .onErrorMap(Exceptions::propagate)
                .log();
    }
}
