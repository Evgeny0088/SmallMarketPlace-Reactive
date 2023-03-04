package com.marketplace.profile.common

interface TestConstants {

    /*
    requests and responses for new-profile endpoint
    */
    String NEW_PROFILE_REQUEST_1 = "requests/new-profile/request_1.json"
    String NEW_PROFILE_REQUEST_2 = "requests/new-profile/request_2.json"
    String NEW_PROFILE_REQUEST_3 = "requests/new-profile/request_3.json"
    String NEW_PROFILE_REQUEST_4 = "requests/new-profile/request_4.json"
    String NEW_PROFILE_REQUEST_5 = "requests/new-profile/request_5.json"
    String NEW_PROFILE_REQUEST_6 = "requests/new-profile/request_6.json"
    String NEW_PROFILE_REQUEST_7 = "requests/new-profile/request_7.json"
    String NEW_PROFILE_REQUEST_8 = "requests/new-profile/request_8.json"
    String NEW_PROFILE_REQUEST_9 = "requests/new-profile/request_9.json"
    String NEW_PROFILE_REQUEST_10 = "requests/new-profile/request_10.json"

    String NEW_PROFILE_RESPONSE_200_1 = "responses/new-profile/response_200_1.json"
    String NEW_PROFILE_RESPONSE_400 = "responses/new-profile/response_400_exists.json"
    String NEW_PROFILE_RESPONSE_400_1 = "responses/new-profile/response_400_1.json"
    String NEW_PROFILE_RESPONSE_400_2 = "responses/new-profile/response_400_2.json"
    String NEW_PROFILE_RESPONSE_400_3 = "responses/new-profile/response_400_3.json"

    /*
    requests and responses for update-profile endpoint
    */
    String UPDATE_PROFILE_REQUEST_1 = "requests/update-profile/request_1.json"
    String UPDATE_PROFILE_REQUEST_2 = "requests/update-profile/request_2.json"
    String UPDATE_PROFILE_REQUEST_3 = "requests/update-profile/request_3.json"
    String UPDATE_PROFILE_REQUEST_4 = "requests/update-profile/request_4.json"
    String UPDATE_PROFILE_REQUEST_5 = "requests/update-profile/request_5.json"
    String UPDATE_PROFILE_REQUEST_6 = "requests/update-profile/request_6.json"
    String UPDATE_PROFILE_REQUEST_7 = "requests/update-profile/request_7.json"
    String UPDATE_PROFILE_REQUEST_8 = "requests/update-profile/request_8.json"
    String UPDATE_PROFILE_REQUEST_9 = "requests/update-profile/request_9.json"

    String UPDATE_PROFILE_RESPONSE_400_1 = "responses/update-profile/response_400_1.json"
    String UPDATE_PROFILE_RESPONSE_400_2 = "responses/update-profile/response_400_2.json"
    String UPDATE_PROFILE_RESPONSE_404 = "responses/update-profile/response_404.json"

    /*
    verify profile requests
    */
    String PROFILE_PASSWORD_FIELD = "password"
    String VERIFY_PROFILE_RESPONSE_400_1 = "responses/verify-profile/response_400_1.json"
    String VERIFY_PROFILE_RESPONSE_400_2 = "responses/verify-profile/response_400_2.json"
    String VERIFY_PROFILE_RESPONSE_400_3 = "responses/verify-profile/response_400_3.json"

    /*
    confirm profile responses
    */
    String CONFIRM_PROFILE_RESPONSE_400_1 = "responses/confirm-profile/response_400_1.json"

    /*
    blocked profile responses
    */
    String BLOCKED_PROFILE_RESPONSE_403 = "responses/blocked-profile/response_403_1.json"

    /*
    request mail again
    */
    String REQUEST_EMAIL_1 = "requests/request-email/request_1.json"
    String REQUEST_EMAIL_2 = "requests/request-email/request_2.json"
    String REQUEST_EMAIL_3 = "requests/request-email/request_3.json"
    String REQUEST_EMAIL_4 = "requests/request-email/request_4.json"
    String REQUEST_EMAIL_5 = "requests/request-email/request_5.json"
    String REQUEST_EMAIL_6 = "requests/request-email/request_6.json"

    String RESPONSE_EMAIL_400_1 = "responses/request-email/response_400_1.json"

    /*
    update profile time conditions
    */
    String TIME_OK = "ok"
    String TOO_EARLY = "too early"
    String TOO_LATE = "too late"

    /*
    roles for test cases
    */
    String[] ROLES_1 = ["USER"]
    String[] ROLES_2 = ["USER", "ADMIN"]
    String[] ROLES_3 = ["USER", "MANAGER"]
    String[] ROLES_4 = ["USER", "ADMIN", "MANAGER"]

    /*
    fields for profile
    */
    String ID = "id"
    String USERNAME = "username"
    String ROLES = "roles"
    String MESSAGE = "message"
    String DETAILS_FIELD = "details"

    String EMPTY = ""
}