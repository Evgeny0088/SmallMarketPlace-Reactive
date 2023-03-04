package com.marketplace.authservice.common

interface TestConstants {

    /*
    registration requests / responses
    */
    String REGISTRATION_REQUEST_200_1 = "requests/registration/request_1.json"
    String REGISTRATION_REQUEST_200_2 = "requests/registration/request_2.json"
    String REGISTRATION_REQUEST_200_5 = "requests/registration/request_5.json"
    String REGISTRATION_REQUEST_200_6 = "requests/registration/request_6.json"
    String REGISTRATION_REQUEST_200_7 = "requests/registration/request_7.json"
    String REGISTRATION_REQUEST_200_8 = "requests/registration/request_8.json"
    String REGISTRATION_REQUEST_200_9 = "requests/registration/request_9.json"
    String REGISTRATION_REQUEST_200_10 = "requests/registration/request_10.json"

    String REGISTRATION_RESPONSE_200_1 = "responses/registration/response_1.json"
    String REGISTRATION_RESPONSE_200_5 = "responses/registration/response_5.json"
    String REGISTRATION_RESPONSE_200_6 = "responses/registration/response_6.json"
    String REGISTRATION_RESPONSE_200_10 = "responses/registration/response_10.json"

    String REGISTRATION_RESPONSE_400_1 = "responses/registration/response_400_1.json"
    String REGISTRATION_RESPONSE_400_2 = "responses/registration/response_400_2.json"
    String REGISTRATION_RESPONSE_400_3 = "responses/registration/response_400_3.json"

    String RESPONSE_503 = "responses/registration/response_503.json"

    /*
    login request / responses
    */
    String LOGIN_MOCKED_RESPONSE_200 = "responses/login/mocked-response_200_1.json"
    String LOGIN_MOCKED_RESPONSE_404 = "responses/login/mocked-response_404.json"
    String LOGIN_MOCKED_RESPONSE_400_1 = "responses/login/mocked-response_400_1.json"
    String LOGIN_MOCKED_RESPONSE_400_2 = "responses/login/mocked-response_400_2.json"
    String LOGIN_MOCKED_RESPONSE_400_3 = "responses/login/mocked-response_400_3.json"

    /*
    request email request / responses
    */
    String REQUEST_EMAIL_1 = "requests/request-email/request_1.json"
    String REQUEST_EMAIL_2 = "requests/request-email/request_2.json"
    String REQUEST_EMAIL_3 = "requests/request-email/request_3.json"
    String REQUEST_EMAIL_4 = "requests/request-email/request_4.json"
    String REQUEST_EMAIL_5 = "requests/request-email/request_5.json"
    String REQUEST_EMAIL_6 = "requests/request-email/request_6.json"
    String REQUEST_EMAIL_7 = "requests/request-email/request_7.json"

    String REQUEST_EMAIL_RESPONSE_400 = "responses/request-email/mocked_response_400_1.json"
    String RESPONSE_EMAIL_200 = "Confirmation email have been sent to your email box";

    /*
    confirm profile changes requests / responses
    */
    String CONFIRM_CHANGES_RESPONSE_200 = "responses/confirm-profile/mocked_response_1.json"
    String CONFIRM_CHANGES_RESPONSE_400 = "responses/confirm-profile/response_400_1.json"
    String CONFIRM_EXP_MESSAGE = "Profile - %s is modified and active!"
    /*
    roles for test cases
    */
    String[] ROLES_1 = ["USER"]
    String[] ROLES_2 = ["USER", "ADMIN"]
    String[] ROLES_3 = ["USER", "MANAGER"]
    String[] ROLES_4 = ["USER", "ADMIN", "MANAGER"]

    String EMPTY = ""
    String PROFILE_ID = "profileId"
    String ACCESS_TOKEN = "access-token"
}