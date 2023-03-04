package com.marketplace.profile.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.ProfileDto
import com.marketplace.exception.lib.exception.CustomExceptionDto
import com.marketplace.profile.common.Constants
import com.marketplace.profile.common.MessageConstants
import com.marketplace.profile.common.TestConstants
import com.marketplace.profile.dto.PageableResponse
import com.marketplace.profile.dto.ResponseDto
import com.marketplace.profile.entity.Profile
import com.marketplace.profile.properties.PageableProperties
import com.marketplace.profile.properties.ProfileUpdateProperties
import com.marketplace.profile.repository.ProfileRepo
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.EntityExchangeResult

import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.LocalDateTime

import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString

class TestVerificationUtils implements TestConstants, Constants, MessageConstants {

    static void verifyNewProfileResponse(EntityExchangeResult<byte[]> response,
                                         ProfileRepo profileRepo,
                                         String expResponse,
                                         HttpStatus expStatus,
                                         String[] expRoles,
                                         ObjectMapper objectMapper, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK ) {
            ProfileDto returnedDto = objectMapper.readValue(response.getResponseBody(), ProfileDto)
            Profile profile = profileRepo.findById(returnedDto.getId()).block()
            assert profile != null
            assert profile.getEmail() == returnedDto.getEmail()
            assert profile.getUsername() == returnedDto.getUsername()
            assert profile.getCreatedAt() != null && returnedDto.getLastModifiedAt() != null

            assert profile.getRoles() == returnedDto.getRoles()
            assert expRoles.stream().allMatch((role)-> returnedDto.getRoles().contains(role))
            assert profile.isActive() == returnedDto.isActive() && !returnedDto.isActive()
            assert !profile.isBlocked()
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyUpdateProfileResponse(EntityExchangeResult<byte[]> response,
                                            ProfileRepo profileRepo,
                                            String expResponse,
                                            String updatedProfileJson,
                                            HttpStatus expStatus,
                                            String[] expRoles,
                                            LocalDateTime previousUpdateTime,
                                            ObjectMapper objectMapper, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK ) {
            Object updatedRequest = objectMapper.readValue(updatedProfileJson, Object)
            Object responseBody = objectMapper.readValue(response.getResponseBody(), Object)
            String id = updatedRequest.getAt(ID)
            String username = updatedRequest.getAt(USERNAME)
            String password = updatedRequest.getAt(PASSWORD)
            Profile profile = profileRepo.findById(id).block()

            assert profile != null
            assert username != null ? profile.getUsername() == username : true
            assert password != null ? profile.getPassword() == password : true
            assert expRoles.stream().allMatch((role)-> profile.getRoles().contains(role))
            assert profile.isActive()
            assert !profile.isBlocked()
            assert profile.getUpdatedAt().isAfter(previousUpdateTime)
            assert responseBody.getAt(MESSAGE) == PROFILE_UPDATE_MESSAGE
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyDeleteProfileResponse(EntityExchangeResult<byte[]> response,
                                            String profileId,
                                            HttpStatus expStatus,
                                            String expResponse,
                                            ObjectMapper objectMapper,
                                            ProfileRepo profileRepo, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            String expectedMessage = String.format(PROFILE_IS_DELETED, profileId)
            assert objectMapper.readValue(response.getResponseBody(), ResponseDto).getContent().contains(expectedMessage)
            assert profileRepo.findById(profileId).block() == null
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyVerifyProfileResponse(EntityExchangeResult<byte[]> response,
                                            ObjectMapper objectMapper,
                                            ProfileRepo profileRepo,
                                            String expResponse,
                                            HttpStatus expStatus,
                                            String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ProfileDto profileFromResponse = objectMapper.readValue(response.getResponseBody(), ProfileDto)
            assert profileRepo.findProfileByUsername(profileFromResponse.getUsername()).block() != null
            assert profileFromResponse.getPassword() == null
            assert profileFromResponse.getEmail() == PROFILE_EMAIL
            assert profileFromResponse.getLastModifiedAt() != null
            assert profileFromResponse.isActive()
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyConfirmProfileResponse(EntityExchangeResult<byte[]> response,
                                        ObjectMapper objectMapper,
                                        ProfileRepo profileRepo,
                                        String profileId,
                                        String expResponse,
                                        HttpStatus expStatus,
                                        String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<String> responseDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            Profile profile = profileRepo.findById(profileId).block()
            String expectedMessage = String.format(PROFILE_UPDATES_CONFIRMED, profile.getId())
            assert responseDto.getMessage() == expectedMessage
            assert profile.isActive()
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyBlockedProfileResponse(EntityExchangeResult<byte[]> response,
                                             ObjectMapper objectMapper,
                                             ProfileRepo profileRepo,
                                             String profileId,
                                             String expectedMessage,
                                             String expResponse,
                                             HttpStatus expStatus,
                                             boolean verifyForBlock,
                                             String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<String> responseDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            Profile profile = profileRepo.findById(profileId).block()
            assert responseDto.getContent().toString() == String.format(expectedMessage, profile.getId())
            assert verifyForBlock ? !profile.isActive() : profile.isActive()
            assert verifyForBlock ? profile.isBlocked() : !profile.isBlocked()
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyRequestMailResponse(EntityExchangeResult<byte[]> response,
                                          ObjectMapper objectMapper,
                                          ProfileRepo profileRepo,
                                          String profileId,
                                          LocalDateTime previousEmailTime,
                                          String expectedMessage,
                                          String expResponse,
                                          HttpStatus expStatus,
                                          String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            String respMessage = new String(response.getResponseBody(), StandardCharsets.UTF_8)
            Profile profile = profileRepo.findById(profileId).block()

            assert respMessage == expectedMessage
            assert profile.getEmailSendAt().isAfter(previousEmailTime)
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyPageableProfileListResponse(EntityExchangeResult<byte[]> response,
                                                  ProfileUpdateProperties updateProperties,
                                                  ObjectMapper objectMapper,
                                                  String expectedMessage,
                                                  int page, int size, boolean emptyList,
                                                  String expResponse,
                                                  HttpStatus expStatus,
                                                  String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<PageableResponse> responseDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            PageableResponse pageableResponse = (PageableResponse) responseDto.getContent()
            PageableProperties pageableProperties = updateProperties.getPageableProperties()
            int maxAllowedPageSize = updateProperties.getPageableProperties().getMaxSizePerPage()

            assert verifyPageableSize(size, maxAllowedPageSize, pageableResponse, pageableProperties)
            assert page >= 0 ? pageableResponse.getCurrentPage() == page : pageableResponse.getCurrentPage() == pageableProperties.getDefaultPage()

            assert responseDto.getMessage() == expectedMessage

            if (emptyList) {
                assert pageableResponse.getProfileList().size() == 0
            }
            else {
                Profile latestProfile = objectMapper.convertValue(pageableResponse.getProfileList().get(0), Profile)
                assert pageableResponse.getProfileList().stream()
                        .map((obj)-> objectMapper.convertValue(obj, Profile))
                        .allMatch((profile)-> Duration.between(profile.getUpdatedAt(), latestProfile.getUpdatedAt()).toNanos() >= 0)
            }
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    private static void verifyJsonResponse (ObjectMapper objectMapper,
                                            EntityExchangeResult<byte[]> response,
                                            String expResponse,
                                            String ... ignoredFields) {
        String[] ignored = ignoredFields != null ? Arrays.asList(ignoredFields) : new String[0]
        Customization[] customizations = Arrays.stream(ignored).map(field-> new Customization(field, (f1, f2) -> true)).toArray(Customization.class)

        String errorStringify = objectMapper.writeValueAsString(objectMapper.readValue(response.getResponseBody(), CustomExceptionDto))
        String expResponseString = readFileAsString(expResponse)

        if (customizations.length > 0) {
            CustomComparator comparator = new CustomComparator(JSONCompareMode.STRICT, customizations)
            JSONAssert.assertEquals(expResponseString, errorStringify, comparator)
        }
        else {
            JSONAssert.assertEquals(expResponseString, errorStringify, JSONCompareMode.STRICT_ORDER)
        }
    }

    private static boolean verifyPageableSize(int size, int maxAllowedPageSize, PageableResponse pageableResponse, PageableProperties pageableProperties) {
        if (size <= 0 ) {
            return pageableResponse.getSize() == pageableProperties.getDefaultSize()
        }
        else if (size > maxAllowedPageSize) {
            return pageableResponse.getSize() == pageableProperties.getDefaultSize()
        }
        else {
            return pageableResponse.getSize() == size
        }
    }

}
