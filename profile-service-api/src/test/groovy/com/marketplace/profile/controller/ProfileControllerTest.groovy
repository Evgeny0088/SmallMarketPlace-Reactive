package com.marketplace.profile.controller

import com.marketplace.ProfileDto
import com.marketplace.profile.BaseSpecification
import com.marketplace.profile.entity.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Stepwise

import java.util.function.Consumer

import static com.marketplace.profile.utils.HelpServiceMethods.uuid
import static com.marketplace.profile.utils.ProfileServiceTestUtils.headersSetup
import static com.marketplace.profile.utils.ProfileServiceTestUtils.jsonReplaceFieldsUpdateProfileRequest
import static com.marketplace.profile.utils.ProfileServiceTestUtils.prepareProfile
import static com.marketplace.profile.utils.ProfileServiceTestUtils.prepareProfileBatch
import static com.marketplace.profile.utils.ProfileServiceTestUtils.prepareProfileDto
import static com.marketplace.profile.utils.ProfileServiceTestUtils.setupRequestMailTime
import static com.marketplace.profile.utils.ProfileServiceTestUtils.setupUpdatedTime
import static com.marketplace.profile.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString
import static com.marketplace.profile.utils.TestVerificationUtils.*
import static com.marketplace.test.helper.starter.utils.HelperUtils.replaceFieldsInJsonString

@Stepwise
class ProfileControllerTest extends BaseSpecification {

    def setup() {
        profileRepo.deleteAll().block()
    }

    def "register new profile"() {
        given:
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(SAVE_PROFILE_PATH)
        Object requestBody = requestJson != null ? testObjectMapper.readValue(readFileAsString(requestJson), Object) : null
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)

        when:
        if (postedAgain) {
            Profile profile = prepareProfile(testObjectMapper, requestBody, profileMapper, false, false)
            profileRepo.save(profile).block()
        }
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestBody)

        then:
        verifyNewProfileResponse(result, profileRepo, expResponse, expStatus, expRoles, testObjectMapper, DETAILS_FIELD)

        where:
        requestJson           | postedAgain | expRoles | expResponse                | expStatus
        NEW_PROFILE_REQUEST_1 | false       | ROLES_1  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_2 | false       | ROLES_1  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_3 | false       | ROLES_1  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_4 | false       | ROLES_1  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_5 | false       | ROLES_2  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_6 | false       | ROLES_4  | EMPTY                      | HttpStatus.OK
        NEW_PROFILE_REQUEST_10| false       | ROLES_3  | EMPTY                      | HttpStatus.OK

        NEW_PROFILE_REQUEST_7 | false       | ROLES_1  | NEW_PROFILE_RESPONSE_400_1 | HttpStatus.BAD_REQUEST
        NEW_PROFILE_REQUEST_8 | false       | ROLES_1  | NEW_PROFILE_RESPONSE_400_2 | HttpStatus.BAD_REQUEST
        NEW_PROFILE_REQUEST_9 | false       | ROLES_1  | NEW_PROFILE_RESPONSE_400_3 | HttpStatus.BAD_REQUEST

        NEW_PROFILE_REQUEST_1 | true        | ROLES_1  | NEW_PROFILE_RESPONSE_400   | HttpStatus.BAD_REQUEST
    }

    def "update profile" () {
        given:
        String requestPathOnUpdate = baseUrl().concat(COMMON_PATH_PREFIX).concat(UPDATE_PROFILE_PATH)
        Object requestJsonOnSave = requestForSave != null ? testObjectMapper.readValue(readFileAsString(requestForSave), Object) : null
        String requestJsonOnUpdate = requestForUpdate != null ? readFileAsString(requestForUpdate) : null

        setupUpdatedTime(updatedTime, profileProperties)

        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, true, false)
        profile.setRoles(currentRoles.join(ROLE_DELIMITER))
        Profile savedProfile = profileRepo.save(profile).block()

        if (!profileFound) profileRepo.delete(savedProfile).block()

        String currentProfile = isOwner ? savedProfile.getId() : uuid()
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, currentProfile, savedProfile.getRoles())
        String updatedProfileJson = jsonReplaceFieldsUpdateProfileRequest(requestJsonOnUpdate, savedProfile)

        when:
        def response = callServiceForResponse(HttpMethod.PATCH, expStatus, webTestClient, requestPathOnUpdate, headers, updatedProfileJson)

        then:
        verifyUpdateProfileResponse(response, profileRepo, expResponse, updatedProfileJson, expStatus, expRoles,
                                                                                savedProfile.getUpdatedAt(), testObjectMapper, DETAILS_FIELD)

        where:
        requestForSave        | requestForUpdate         | updatedTime | isOwner | profileFound | currentRoles | expRoles | expResponse                 | expStatus
        NEW_PROFILE_REQUEST_1 | UPDATE_PROFILE_REQUEST_1 | TIME_OK     | true    | true         | ROLES_1      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_2 | UPDATE_PROFILE_REQUEST_2 | TIME_OK     | true    | true         | ROLES_1      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_3 | UPDATE_PROFILE_REQUEST_3 | TIME_OK     | true    | true         | ROLES_1      | ROLES_3  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_4 | UPDATE_PROFILE_REQUEST_4 | TIME_OK     | true    | true         | ROLES_1      | ROLES_3  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_5 | TIME_OK     | true    | true         | ROLES_2      | ROLES_2  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_5 | TIME_OK     | false   | true         | ROLES_2      | ROLES_2  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_6 | UPDATE_PROFILE_REQUEST_6 | TIME_OK     | true    | true         | ROLES_4      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_6 | UPDATE_PROFILE_REQUEST_6 | TIME_OK     | false   | true         | ROLES_4      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_6 | UPDATE_PROFILE_REQUEST_7 | TIME_OK     | true    | true         | ROLES_4      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_6 | UPDATE_PROFILE_REQUEST_7 | TIME_OK     | false   | true         | ROLES_4      | ROLES_1  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_1 | UPDATE_PROFILE_REQUEST_8 | TIME_OK     | true    | true         | ROLES_1      | ROLES_4  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_9 | TIME_OK     | true    | true         | ROLES_1      | ROLES_3  | EMPTY                       | HttpStatus.OK
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_9 | TIME_OK     | false   | true         | ROLES_2      | ROLES_3  | EMPTY                       | HttpStatus.OK

        NEW_PROFILE_REQUEST_4 | UPDATE_PROFILE_REQUEST_5 | TIME_OK     | false   | true         | ROLES_1      | ROLES_2  |UPDATE_PROFILE_RESPONSE_400_1| HttpStatus.BAD_REQUEST
        NEW_PROFILE_REQUEST_10| UPDATE_PROFILE_REQUEST_2 | TIME_OK     | false   | true         | ROLES_3      | ROLES_1  |UPDATE_PROFILE_RESPONSE_400_1| HttpStatus.BAD_REQUEST
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_9 | TOO_EARLY   | true    | true         | ROLES_2      | ROLES_3  |UPDATE_PROFILE_RESPONSE_400_2| HttpStatus.BAD_REQUEST
        NEW_PROFILE_REQUEST_5 | UPDATE_PROFILE_REQUEST_9 | TOO_EARLY   | false   | true         | ROLES_2      | ROLES_3  |UPDATE_PROFILE_RESPONSE_400_2| HttpStatus.BAD_REQUEST

        NEW_PROFILE_REQUEST_1 | UPDATE_PROFILE_REQUEST_1 | TIME_OK     | true    | false        | ROLES_1      | ROLES_1  | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
    }

    def "delete profile" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_1), Object)
        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, true, false)
        profile.setRoles(currentRoles.join(ROLE_DELIMITER))
        Profile savedProfile = profileRepo.save(profile).block()

        String currentProfile = isOwner ? savedProfile.getId() : uuid()
        String profileId = profileFound ? savedProfile.getId() : uuid()

        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat("/delete-profile/${profileId}")
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, currentProfile, savedProfile.getRoles())

        when:
        def response = callServiceForResponse(HttpMethod.DELETE, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyDeleteProfileResponse(response, savedProfile.getId(), expStatus, expResponse, testObjectMapper, profileRepo, DETAILS_FIELD)

        where:
        isOwner | profileFound | currentRoles | expResponse           | expStatus
        true    | true         | ROLES_1      | EMPTY                 | HttpStatus.OK
        false   | true         | ROLES_2      | EMPTY                 | HttpStatus.OK
        false   | true         | ROLES_4      | EMPTY                 | HttpStatus.OK

        false   | true         | ROLES_3      |UPDATE_PROFILE_RESPONSE_400_1| HttpStatus.BAD_REQUEST
        true    | false        | ROLES_2      | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
    }

    def "verify profile for authentication" () {
        given:
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(VERIFY_PROFILE_PATH)
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_5), Object)
        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, active, blocked)
        profile.setRoles(ROLES_2.join(ROLE_DELIMITER))
        Profile savedProfile = profileRepo.save(profile).block()

        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)
        String password = requestJsonOnSave.getAt(PROFILE_PASSWORD_FIELD)
        ProfileDto requestBody = prepareProfileDto(savedProfile, password, profileFound, passOK)

        when:
        def response = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestBody)

        then:
        verifyVerifyProfileResponse(response, testObjectMapper, profileRepo, expResponse, expStatus, DETAILS_FIELD)

        where:
        profileFound | passOK | active | blocked | expResponse                 | expStatus
        true         | true   | true   | false   | EMPTY                       | HttpStatus.OK
        false        | true   | true   | false   | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        true         | false  | true   | false   |VERIFY_PROFILE_RESPONSE_400_1| HttpStatus.BAD_REQUEST
        true         | true   | false  | false   |VERIFY_PROFILE_RESPONSE_400_2| HttpStatus.BAD_REQUEST
        true         | true   | true   | true    |VERIFY_PROFILE_RESPONSE_400_3| HttpStatus.BAD_REQUEST

    }

    def "request mail again" () {
        given:
        Object profileDtoOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_1), Object)
        Profile profile = prepareProfile(testObjectMapper, profileDtoOnSave, profileMapper, true, false)
        profile.setRoles(ROLES_1.join(ROLE_DELIMITER))
        Profile savedProfile = profileRepo.save(profile).block()

        String profileId = profileFound ? savedProfile.getId() : uuid()
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(REQUEST_EMAIL_AGAIN)
        String requestJson = replaceFieldsInJsonString(readFileAsString(requestEmail), Map.of(ID, profileId))
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)

        setupRequestMailTime(requestTime, profileProperties)

        when:
        def response = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestJson)

        then:
        verifyRequestMailResponse(response, testObjectMapper, profileRepo, profileId, savedProfile.getEmailSendAt(), CONFIRMATION_EMAIL_MESSAGE, expResponse, expStatus,
                                                            expStatus == HttpStatus.NOT_FOUND ? DETAILS_FIELD : null)

        where:
        requestEmail    | profileFound | requestTime | expResponse                 | expStatus
        REQUEST_EMAIL_1 | true         | TIME_OK     | EMPTY                       | HttpStatus.OK
        REQUEST_EMAIL_2 | true         | TIME_OK     | EMPTY                       | HttpStatus.OK

        REQUEST_EMAIL_1 | false        | TIME_OK     | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_3 | true         | TIME_OK     | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_4 | true         | TIME_OK     | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_5 | true         | TIME_OK     | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_6 | true         | TIME_OK     | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND

        REQUEST_EMAIL_1 | true         | TOO_EARLY   | RESPONSE_EMAIL_400_1        | HttpStatus.BAD_REQUEST

    }

    def "confirm profile changes" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_2), Object)
        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, false, false)
        profile.setRoles(ROLES_1.join(ROLE_DELIMITER))
        Profile savedProfile = profileRepo.save(profile).block()

        setupUpdatedTime(emailTime, profileProperties)

        when:
        String profileId = profileFound ? savedProfile.getId() : uuid()
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat("/update-profile/${profileId}/confirm")
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyConfirmProfileResponse(response, testObjectMapper, profileRepo, profileId, expResponse, expStatus,
                                                        expStatus == HttpStatus.BAD_REQUEST ? null : DETAILS_FIELD)

        where:
        profileFound | emailTime | expResponse                 | expStatus
        true         | TIME_OK   | EMPTY                       | HttpStatus.OK
        false        | TIME_OK   | UPDATE_PROFILE_RESPONSE_404 | HttpStatus.NOT_FOUND
        true         | TOO_LATE  | CONFIRM_PROFILE_RESPONSE_400_1 | HttpStatus.BAD_REQUEST
    }

    def "block profile" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_3), Object)
        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, false, alreadyBlocked)
        String stringifyRoles = currentRoles.join(ROLE_DELIMITER)
        profile.setRoles(stringifyRoles)
        Profile savedProfile = profileRepo.save(profile).block()
        String blockedCandidate = savedProfile.getId()
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat("/block-profile/${profileFound ? blockedCandidate : uuid()}")
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, stringifyRoles)

        when:
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyBlockedProfileResponse(response, testObjectMapper, profileRepo, blockedCandidate, PROFILE_IS_BLOCKED, expResposne, expStatus,
                                                                                true, expStatus == HttpStatus.FORBIDDEN ? null : DETAILS_FIELD)

        where:
        profileFound | alreadyBlocked | currentRoles | expResposne               | expStatus
        true         | false          | ROLES_2      | EMPTY                     | HttpStatus.OK
        true         | false          | ROLES_4      | EMPTY                     | HttpStatus.OK
        true         | true           | ROLES_4      | EMPTY                     | HttpStatus.OK

        false        | false          | ROLES_2      | UPDATE_PROFILE_RESPONSE_404  | HttpStatus.NOT_FOUND
        true         | false          | ROLES_1      | BLOCKED_PROFILE_RESPONSE_403 | HttpStatus.FORBIDDEN
        true         | true           | ROLES_3      | BLOCKED_PROFILE_RESPONSE_403 | HttpStatus.FORBIDDEN
    }

    def "unblock profile" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_3), Object)
        Profile profile = prepareProfile(testObjectMapper, requestJsonOnSave, profileMapper, false, alreadyBlocked)
        String stringifyRoles = currentRoles.join(ROLE_DELIMITER)
        profile.setRoles(stringifyRoles)
        Profile savedProfile = profileRepo.save(profile).block()
        String unblockedCandidate = savedProfile.getId()
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat("/unblock-profile/${profileFound ? unblockedCandidate : uuid()}")
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, stringifyRoles)

        when:
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyBlockedProfileResponse(response, testObjectMapper, profileRepo, unblockedCandidate, PROFILE_IS_UNBLOCKED, expResposne, expStatus,
                                                                                    false, expStatus == HttpStatus.FORBIDDEN ? null : DETAILS_FIELD)
        where:
        profileFound | alreadyBlocked | currentRoles | expResposne               | expStatus
        true         | false          | ROLES_2      | EMPTY                     | HttpStatus.OK
        true         | false          | ROLES_4      | EMPTY                     | HttpStatus.OK
        true         | true           | ROLES_4      | EMPTY                     | HttpStatus.OK

        false        | false          | ROLES_2      | UPDATE_PROFILE_RESPONSE_404  | HttpStatus.NOT_FOUND
        true         | false          | ROLES_1      | BLOCKED_PROFILE_RESPONSE_403 | HttpStatus.FORBIDDEN
        true         | true           | ROLES_3      | BLOCKED_PROFILE_RESPONSE_403 | HttpStatus.FORBIDDEN
    }

    def "get all unblocked profiles" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_2), Object)
        List<Profile> unblockedProfiles = prepareProfileBatch(unblockedSize, testObjectMapper, requestJsonOnSave, profileMapper, true, false)
        List<Profile> blockedProfiles = prepareProfileBatch(2, testObjectMapper, requestJsonOnSave, profileMapper, false, true)
        unblockedProfiles.addAll(blockedProfiles)
        unblockedProfiles.stream().forEach (profile-> profileRepo.save(profile).subscribe())
        Thread.sleep(300)

        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(GET_ALL_UNBLOCKED_PROFILES_PATH).concat("?page=$page&size=$size")
        String stringifyRoles = currentRoles.join(ROLE_DELIMITER)
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, stringifyRoles)

        when:
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyPageableProfileListResponse(response, profileProperties, testObjectMapper, UNBLOCKED_PROFILE_LIST,
                                                                page, size, emptyList, expResponse, expStatus, null)

        where:
        page | size | unblockedSize | emptyList | currentRoles | expResponse                | expStatus
        0    | 3    | 10            | false     | ROLES_2      | EMPTY                      | HttpStatus.OK
        0    | 3    | 10            | false     | ROLES_3      | EMPTY                      | HttpStatus.OK
        0    | 3    | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        1    | 5    | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        2    | 5    | 10            | true      | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 5    | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | -1   | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 0    | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 100  | 10            | false     | ROLES_4      | EMPTY                      | HttpStatus.OK

        2    | 5    | 10            | true      | ROLES_1      |BLOCKED_PROFILE_RESPONSE_403| HttpStatus.FORBIDDEN
    }

    def "get all blocked profiles" () {
        given:
        Object requestJsonOnSave = testObjectMapper.readValue(readFileAsString(NEW_PROFILE_REQUEST_2), Object)
        List<Profile> blockedProfiles = prepareProfileBatch(blockedSize, testObjectMapper, requestJsonOnSave, profileMapper, false, true)
        List<Profile> unblockedProfiles = prepareProfileBatch(2, testObjectMapper, requestJsonOnSave, profileMapper, true, false)
        blockedProfiles.addAll(unblockedProfiles)
        blockedProfiles.stream().forEach (profile-> profileRepo.save(profile).subscribe())
        Thread.sleep(300)

        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(GET_ALL_BLOCKED_PROFILES_PATH).concat("?page=$page&size=$size")
        String stringifyRoles = currentRoles.join(ROLE_DELIMITER)
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, stringifyRoles)

        when:
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyPageableProfileListResponse(response, profileProperties, testObjectMapper, BLOCKED_PROFILE_LIST,
                page, size, emptyList, expResponse, expStatus, null)

        where:
        page | size | blockedSize | emptyList | currentRoles | expResponse                | expStatus
        0    | 3    | 10          | false     | ROLES_2      | EMPTY                      | HttpStatus.OK
        0    | 3    | 10          | false     | ROLES_3      | EMPTY                      | HttpStatus.OK
        0    | 3    | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        1    | 5    | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        2    | 5    | 10          | true      | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 5    | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | -1   | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 0    | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK
        -1   | 100  | 10          | false     | ROLES_4      | EMPTY                      | HttpStatus.OK

        2    | 5    | 10          | true      | ROLES_1      |BLOCKED_PROFILE_RESPONSE_403| HttpStatus.FORBIDDEN

    }
}
