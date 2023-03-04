package com.marketplace.authservice.controller

import com.marketplace.authservice.BaseSpecification
import com.marketplace.authservice.common.Constants
import com.marketplace.authservice.common.TestConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

import java.util.function.Consumer

import static com.marketplace.authservice.utils.AuthServiceTestUtils.headersSetup
import static com.marketplace.authservice.utils.TestVerificationUtils.*
import static com.marketplace.authservice.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString
import static com.marketplace.test.helper.starter.utils.HelperUtils.wireMockStubs

class AuthControllerTest extends BaseSpecification implements TestConstants, Constants {

    def "registration" () {
        given:
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(REGISTRATION_PATH)
        String newProfilePath = String.format(SAVE_PROFILE_PATH, COMMON_PREFIX_PROFILE)

        String requestJsonBody = readFileAsString(requestJson)
        String mockedJsonResponse = readFileAsString(mockedResponse)

        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON)
        wireMockStubs(mockedJsonResponse, newProfilePath, mockedStatus, HttpMethod.POST)

        when:
        def response = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestJsonBody)

        then:
        verifyNewProfileResponse(response,expResponse, REGISTRATION_MESSAGE_RESPONSE_200, expStatus, expRoles, testObjectMapper, null)

        where:
        requestJson                | mockedResponse             | mockedStatus  | expRoles | expResponse | expStatus
        REGISTRATION_REQUEST_200_1 | REGISTRATION_RESPONSE_200_1| HttpStatus.OK | ROLES_1  | EMPTY       | HttpStatus.OK
        REGISTRATION_REQUEST_200_2 | REGISTRATION_RESPONSE_200_1| HttpStatus.OK | ROLES_1  | EMPTY       | HttpStatus.OK
        REGISTRATION_REQUEST_200_5 | REGISTRATION_RESPONSE_200_5| HttpStatus.OK | ROLES_2  | EMPTY       | HttpStatus.OK
        REGISTRATION_REQUEST_200_6 | REGISTRATION_RESPONSE_200_6| HttpStatus.OK | ROLES_3  | EMPTY       | HttpStatus.OK
        REGISTRATION_REQUEST_200_10|REGISTRATION_RESPONSE_200_10| HttpStatus.OK | ROLES_4  | EMPTY       | HttpStatus.OK

        REGISTRATION_REQUEST_200_7 |REGISTRATION_RESPONSE_400_1 | HttpStatus.BAD_REQUEST | ROLES_4 | REGISTRATION_RESPONSE_400_1 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_8 |REGISTRATION_RESPONSE_400_2 | HttpStatus.BAD_REQUEST | ROLES_4 | REGISTRATION_RESPONSE_400_2 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_9 |REGISTRATION_RESPONSE_400_3 | HttpStatus.BAD_REQUEST | ROLES_4 | REGISTRATION_RESPONSE_400_3 | HttpStatus.BAD_REQUEST

        REGISTRATION_REQUEST_200_1 | RESPONSE_503               | HttpStatus.SERVICE_UNAVAILABLE | ROLES_4  | RESPONSE_503 | HttpStatus.SERVICE_UNAVAILABLE
    }

    def "login" () {
        given:
        String verifyProfilePath = String.format(VERIFY_PROFILE_PATH, COMMON_PREFIX_PROFILE)
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(LOGIN_PATH)
        String requestJson = readFileAsString(loginRequest)
        wireMockStubs(readFileAsString(mockedResponse), verifyProfilePath, expStatus, HttpMethod.POST)

        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON)

        when:
        def response = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestJson)

        then:
        verifyLoginResponse(response, expResponse, ACCESS_TOKEN_IS_PROVIDED, expStatus, testObjectMapper, null)

        where:
        loginRequest               | mockedResponse            | expResponse         | expStatus
        REGISTRATION_REQUEST_200_1 | LOGIN_MOCKED_RESPONSE_200 | EMPTY               | HttpStatus.OK

        REGISTRATION_REQUEST_200_1 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND

        REGISTRATION_REQUEST_200_1 |LOGIN_MOCKED_RESPONSE_400_1| LOGIN_MOCKED_RESPONSE_400_1 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_1 |LOGIN_MOCKED_RESPONSE_400_2| LOGIN_MOCKED_RESPONSE_400_2 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_1 |LOGIN_MOCKED_RESPONSE_400_3| LOGIN_MOCKED_RESPONSE_400_3 | HttpStatus.BAD_REQUEST

        REGISTRATION_REQUEST_200_7 |LOGIN_MOCKED_RESPONSE_200  | REGISTRATION_RESPONSE_400_1 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_8 |LOGIN_MOCKED_RESPONSE_200  | REGISTRATION_RESPONSE_400_2 | HttpStatus.BAD_REQUEST
        REGISTRATION_REQUEST_200_9 |LOGIN_MOCKED_RESPONSE_200  | REGISTRATION_RESPONSE_400_3 | HttpStatus.BAD_REQUEST

        REGISTRATION_REQUEST_200_1 | RESPONSE_503              | RESPONSE_503                | HttpStatus.SERVICE_UNAVAILABLE
    }

    def "request mail again" () {
        given:
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(REQUEST_EMAIL_AGAIN)
        String requestEmailPath = String.format(REQUEST_EMAIL_AGAIN_PATH, COMMON_PREFIX_PROFILE)
        String requestEmailJson = readFileAsString(emailRequest)
        String mockedResponseJson = expStatus == HttpStatus.OK ? mockedResponse : readFileAsString(mockedResponse as String)
        wireMockStubs(mockedResponseJson, requestEmailPath, expStatus, HttpMethod.POST)

        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON)

        when:
        def response = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, requestPath, headers, requestEmailJson)

        then:
        verifyRequestEmailResponse(response, expResponse, RESPONSE_EMAIL_200, expStatus, testObjectMapper, null)

        where:
        emailRequest    | mockedResponse     | expResponse | expStatus
        REQUEST_EMAIL_1 | RESPONSE_EMAIL_200 | EMPTY       | HttpStatus.OK
        REQUEST_EMAIL_2 | RESPONSE_EMAIL_200 | EMPTY       | HttpStatus.OK

        REQUEST_EMAIL_1 | REQUEST_EMAIL_RESPONSE_400| REQUEST_EMAIL_RESPONSE_400| HttpStatus.BAD_REQUEST

        REQUEST_EMAIL_3 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_4 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_5 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_6 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND
        REQUEST_EMAIL_7 | LOGIN_MOCKED_RESPONSE_404 | LOGIN_MOCKED_RESPONSE_404 | HttpStatus.NOT_FOUND

        REQUEST_EMAIL_1 | RESPONSE_503              | RESPONSE_503              | HttpStatus.SERVICE_UNAVAILABLE
    }

    def "confirm profile changes" () {
        given:
        String profileId = PROFILE_ID
        String confirmPathSegment = "/update-profile/${profileId}/confirm"
        String requestPath = baseUrl().concat(COMMON_PATH_PREFIX).concat(confirmPathSegment)
        String confirmProfilePath = COMMON_PREFIX_PROFILE.concat(confirmPathSegment)

        String expMessage = String.format(CONFIRM_EXP_MESSAGE, profileId)
        String mockedResponseJson = readFileAsString(mockedResponse)
        wireMockStubs(mockedResponseJson, confirmProfilePath, expStatus, HttpMethod.GET)

        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON)

        when:
        def response = callServiceForResponse(HttpMethod.GET, expStatus, webTestClient, requestPath, headers, null)

        then:
        verifyRequestEmailResponse(response, expResponse, expMessage, expStatus, testObjectMapper, null)

        where:
        mockedResponse               | expResponse                 | expStatus
        CONFIRM_CHANGES_RESPONSE_200 | EMPTY                       | HttpStatus.OK

        CONFIRM_CHANGES_RESPONSE_400 | CONFIRM_CHANGES_RESPONSE_400| HttpStatus.BAD_REQUEST
        LOGIN_MOCKED_RESPONSE_404    | LOGIN_MOCKED_RESPONSE_404   | HttpStatus.NOT_FOUND

    }
}
