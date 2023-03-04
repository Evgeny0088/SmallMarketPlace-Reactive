package com.marketplace.profile.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.ProfileDto
import com.marketplace.profile.common.Constants
import com.marketplace.profile.common.TestConstants
import com.marketplace.profile.entity.Profile
import com.marketplace.profile.mapper.ProfileMapper
import com.marketplace.profile.properties.ProfileUpdateProperties
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.spockframework.runtime.IFeatureFilter
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import java.time.LocalDateTime
import java.util.function.Consumer

import static com.marketplace.profile.utils.HelpServiceMethods.uuid

class ProfileServiceTestUtils implements TestConstants, Constants {

    static List<Profile> prepareProfileBatch(int size,
                                             ObjectMapper objectMapper,
                                             Object requestBody,
                                             ProfileMapper mapper,
                                             boolean isActive,
                                             boolean blocked) {
        List<Profile> profiles = new ArrayList<>()
        for (int i in 0..size-1) {
            Profile p = prepareProfile(objectMapper, requestBody, mapper, isActive, blocked)
            p.setUsername(uuid())
            p.setCreatedAt(LocalDateTime.now().minusMinutes(i))
            p.setUpdatedAt(p.getCreatedAt())
            profiles.add(p)
        }
        return profiles
    }

    static Profile prepareProfile(ObjectMapper objectMapper,
                                  Object requestBody,
                                  ProfileMapper mapper,
                                  boolean isActive,
                                  boolean blocked) {
        ProfileDto profileDto = objectMapper.convertValue(requestBody, ProfileDto)
        Profile savedProfile = mapper.profileDtoToProfile(profileDto)
        savedProfile.setActive(isActive)
        savedProfile.setBlocked(blocked)
        return savedProfile
    }

    static ProfileDto prepareProfileDto(Profile profile,
                                        String password,
                                        boolean profileFound,
                                        boolean passOk) {
        return ProfileDto.builder()
                .username(profileFound ? profile.getUsername() : uuid())
                .password(passOk ? password : uuid())
                .email(PROFILE_EMAIL)
                .build()
    }

    static Consumer<HttpHeaders> headersSetup(MediaType mediaType,
                                              String profileId,
                                              String roles) {
        return (headers) -> {
            headers.setContentType(mediaType)
            if (profileId != null) headers.add(HEADER_PROFILE_ID, profileId)
            if (roles != null) headers.add(ROLES, roles)
        }
    }

    static String jsonReplaceFieldsUpdateProfileRequest(String jsonString,
                                                        Profile savedProfile) {
        def json = new JsonSlurper().parseText(jsonString)
        def builder = new JsonBuilder(json)
        if (builder.getContent().getAt(ID) != null) {
            builder.content[ID] = savedProfile.getId()
        }
        return builder.toString()
    }

    static void setupUpdatedTime(String updatedTime, ProfileUpdateProperties updateProperties) {
        if (updatedTime == TIME_OK) updateProperties.setUpdateAfter(-1)
        if (updatedTime == TOO_EARLY) updateProperties.setUpdateAfter(10)
        if (updatedTime == TOO_LATE) updateProperties.setExpiredEmailAfter(-1)
    }

    static void setupRequestMailTime(String requestTime, ProfileUpdateProperties updateProperties) {
        if (requestTime == TIME_OK) {
            updateProperties.setRequestEmailAfter(-1)
        }
        else updateProperties.setRequestEmailAfter(10)
    }
}
