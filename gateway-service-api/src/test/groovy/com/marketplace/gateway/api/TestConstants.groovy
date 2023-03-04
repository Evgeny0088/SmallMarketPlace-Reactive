package com.marketplace.gateway.api

interface TestConstants {

    String SERVICE_REQUEST = "requests/test-service-request.json"
    String RESPONSE_200 = "responses/response_200.json"
    String RESPONSE_404 = "responses/response_404.json"
    String RESPONSE_401_1 = "responses/response_401_1.json"
    String RESPONSE_401_2 = "responses/response_401_2.json"
    String RESPONSE_401_3 = "responses/response_401_3.json"
    String RESPONSE_401_4 = "responses/response_401_4.json"
    String RESPONSE_401_5 = "responses/response_401_5.json"
    String RESPONSE_401_6 = "responses/response_401_6.json"
    String RESPONSE_401_7 = "responses/response_401_7.json"

    String CONTENT_TYPE = "Content-Type"
    String CLIENT_NAME = "test-name"
    String CLIENT_PASS = "test-pass"
    String INVALID = "invalid"

    String TOKEN_TEST_SECRET = "test-secret"
    long TOKEN_EXPIRES_AFTER_DEFAULT_PROPERTY = 480_000L

    /*
    Invalid token cases
    */

    String CORRECT = "correct"
    String WRONG_SIGN = "wrong signature"
    String INVALID_SECRET = "invalid secret"
    String WRONG_ALG = "wrong alg"
    String EXPIRED = "expired"
    String EMPTY_T = "empty token"
    String BLANK_T = "blank token"
    String BAD_FORMATTING = "bad formatting"

    String WRONG_ALG_TOKEN = "eyJraWQiOiJCaXhITkpBaUF4MGt4UnhzdklxdXFyTEJNNGREMEtOXC9sTUlxSzE2MXFuMD0iLCJhbGciOiJSUzI1NiJ9" +
        ".eyJzdWIiOiI4YmU1MGEyNC1iNmQ5LTQ2MjItYmY3NS0xYjUwOWEzOGNlY2IiLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAudXMtZWFzdC0xLmFtYXpvbm" +
        "F3cy5jb21cL3VzLWVhc3QtMV9KdW12WkxES3EiLCJjbGllbnRfaWQiOiI1c2RtY3Bpa3Z2bjFtaGgxcnZoM2JvbDEycSIsIm9yaWdpbl9qdGkiOiJhNmU1NTkxMy1l" +
        "NGE3LTRhMzQtOTM1NC1lMzZiZWMyM2U2OGIiLCJldmVudF9pZCI6IjI5N2E0OTZlLTMwMTYtNDdhYS1iN2NlLTdiODEwZmEzZmZlZSIsInRva2VuX3VzZSI6ImFjY2Vzcy" +
        "IsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2NjU2OTEwNDYsImV4cCI6MTY2NTY5NDY0NiwiaWF0IjoxNjY1NjkxMDQ2LCJqd" +
        "GkiOiIxODU1YWExYy1iZjAyLTRiZDQtOTkwYy1hMzc2MzgwYmI5NTQiLCJ1c2VybmFtZSI6IjMzYjM3Nzg0LTI1NWItNGUxMS04ZmZmLWUzMDkyMjQ0Y2Q1YyJ9.ox2FBXqJVdX0NAP6w" +
        "RsgZy4Xovxpnx7--iDLb2n3mPKEzcDbHrEkuD6CiIf-jQuf6q1F1c7_kVeWUFw4LYAEJhbt0kAqadhUoWo05oPoF0kFpRpVhL7YyQ9OtEsUI0_qeGoytih1t8YbowNzlPW12nFdaWw23NlvxxilF2" +
        "behwXH4IvzSgwhzo_3UkOQm2IxqrVd8PvhzH7BF2kB7N8w1ov6-EN7Cr1KC6LO2F1jJUkSOgtWPM3zWqBc7hqdlu8BMFgT0f3aRJJOTH5BOGyvEuUVB7tH_LWOZhxEgdChmQIPlGX_VJam1RhwQr6mxWonlpidFBJaooJi4NgF_3v5DQ"

    String WRONG_SIGN_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzcyI6Ii9hcGkvbG9naW4iLCJl" +
            "eHAiOjE2NjUzNDQzNjR9.ZvqGlmqWKR_xDVricW4i9VYT43Iz"

    String EMPTY_TOKEN = ""
    String BLANK_TOKEN = "  "
    String BAD_FORMATTING_TOKEN = "any string can be here"

    /*
    incoming request urls for tests
    positive test cases
     */

    String TEST_INCOMING_URL_1 = "profiles/profile/api/v1/something"
    String TEST_INCOMING_URL_1_EXPECTED = "/api/v1/something"

    String TEST_INCOMING_URL_2 = "PROFILEs/proFILE/api/v1/profiles/something/something"
    String TEST_INCOMING_URL_2_EXPECTED = "/api/v1/profiles/something/something"

    String TEST_INCOMING_URL_3 = "profiles/profile/something/something**__123__something/profile-test/profile"
    String TEST_INCOMING_URL_3_EXPECTED = "/something/something**__123__something/profile-test/profile"

    String TEST_INCOMING_URL_4 = "profiles/profile/v1/something/profiles/123"
    String TEST_INCOMING_URL_4_EXPECTED = "/v1/something/profiles/123"

    String TEST_INCOMING_URL_5 = "profiles/profile/api/v1/something/123-something-123"
    String TEST_INCOMING_URL_5_EXPECTED = "/api/v1/something/123-something-123"

    String TEST_INCOMING_URL_6 = "registration/auth/api/v1/something/*-/registration/acutator"
    String TEST_INCOMING_URL_6_EXPECTED = "/api/v1/something/*-/registration/acutator"

    String TEST_INCOMING_URL_7 = "profiles/profile/api/v1/something/*-/profile-test/intenal"
    String TEST_INCOMING_URL_7_EXPECTED = "/api/v1/something/*-/profile-test/intenal"

    String TEST_INCOMING_URL_8 = "login/auth/api/v1/something/*-/login?arg1=**&arg2=any"
    String TEST_INCOMING_URL_8_EXPECTED = "/api/v1/something/*-/login"

    String TEST_INCOMING_URL_9 = "any/auth/api/v1/something/auth/profile/**?_internal_"
    String TEST_INCOMING_URL_9_EXPECTED = "/api/v1/something/auth/profile/**"

    String TEST_INCOMING_URL_10 = "registration/profile/api/v1/something/auth/profile/args?admin=admin&internal=int"
    String TEST_INCOMING_URL_10_EXPECTED = "/api/v1/something/auth/profile/args"

    /*
    negative test cases
     */

    String TEST_INCOMING_URL_30 = "profiles/profile/api/v1/something/auth/admin/**"
    String TEST_INCOMING_URL_30_EXPECTED = "/api/v1/something/auth/admin/**"

    String TEST_INCOMING_URL_31 = "profiles/profile/api/v1/something/auth/profile/**_internal_"
    String TEST_INCOMING_URL_31_EXPECTED = "/api/v1/something/auth/profile/**_internal_"

    String TEST_INCOMING_URL_32 = "login/auth/api/v1/internal/something"
    String TEST_INCOMING_URL_32_EXPECTED = "/api/v1/internal/something"

    String TEST_INCOMING_URL_33 = "profiles/profile/api/v1/something/internal"
    String TEST_INCOMING_URL_33_EXPECTED = "/api/v1/something/internal"

    String TEST_INCOMING_URL_34 = "profiles/auth/api/v1/something/auth/admin/any"
    String TEST_INCOMING_URL_34_EXPECTED = "/api/v1/something/auth/admin/any"

    String TEST_INCOMING_URL_35 = "profiles/auth/api/v1/something/__actuator__?arg1=actuator"
    String TEST_INCOMING_URL_35_EXPECTED = "/api/v1/something/__actuator__"

    String TEST_INCOMING_URL_36 = "profiles/profile/api/v1/something//profile/ACTUATOR?arg1=any-string"
    String TEST_INCOMING_URL_36_EXPECTED = "/api/v1/something//profile/ACTUATOR"

    String TEST_INCOMING_URL_37 = "profiles/auth/api/v1/intERnal/something//profile/ADMIN__"
    String TEST_INCOMING_URL_37_EXPECTED = "/api/v1/internal/something//profile/ADMIN__"

    String TEST_INCOMING_URL_38 = "profile/wrong-alias/api/v1/something//profile/any"
    String TEST_INCOMING_URL_38_EXPECTED = "/api/v1/something//profile/any"

    String TEST_INCOMING_URL_39 = "any-location/wrong-alias/api/v1/something//profile/any"
    String TEST_INCOMING_URL_39_EXPECTED = "/api/v1/something//profile/any"

    /*
    incoming urls for verification not-required paths
    */
    String OPEN_INCOMING_URL_1 = "registration/auth/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/confirm"
    String OPEN_INCOMING_URL_1_EXPECTED = "/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365253ae19b/confirm"

    String OPEN_INCOMING_URL_2 = "market/profile/profile/a68dbcf1-2d00-4aa7-a6e4-0365253ae19b/confirm"
    String OPEN_INCOMING_URL_2_EXPECTED = "/profile/a68dbcf1-2d00-4aa7-a6e4-0365253ae19b/confirm"

    String OPEN_INCOMING_URL_3 = "registration/profile/auth/profile/auth/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_3_EXPECTED = "/auth/profile/auth/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_4 = "registration/profile/profile/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_4_EXPECTED = "/profile/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_5 = "registration/profile/profile/a68dbcdx-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_5_EXPECTED = "/profile/a68dbcdx-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_6 = "registration/profile/profile/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm/any"
    String OPEN_INCOMING_URL_6_EXPECTED = "/profile/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm/any"

    String OPEN_INCOMING_URL_7 = "registration/profile/auth/any/any-string/confirm"
    String OPEN_INCOMING_URL_7_EXPECTED = "/auth/any/any-string/confirm"

    String OPEN_INCOMING_URL_8 = "registration/profile/aut/any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_8_EXPECTED = "/aut/any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_9 = "registration/profile/AUTH/Any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_9_EXPECTED = "/auth/any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_10 = "reg/any-prefix/internal/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_10_EXPECTED = "/internal/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_11 = "reg/any-prefix/actuator/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_11_EXPECTED = "/actuator/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_12 = "reg/any-prefix/admin/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"
    String OPEN_INCOMING_URL_12_EXPECTED = "/admin/auth/any/a68dbcd2-2d54-4aa7-a6e4-0365255ae19b/confirm"

    String OPEN_INCOMING_URL_13 = "registration/profile/aut/any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm?arg=any"
    String OPEN_INCOMING_URL_13_EXPECTED = "/aut/any/a68dbcdf-2d54-4aa7-a6e4-0365255ae19b/confirm"

    /*
    incoming urls for verification is-required paths
    */
    String CLOSED_INCOMING_URL_1 = "registration/auth/auth/api-docs/*"
    String CLOSED_INCOMING_URL_1_EXPECTED = "/auth/api-docs/*"

    String CLOSED_INCOMING_URL_2 = "any/auth/auth/api-docs"
    String CLOSED_INCOMING_URL_2_EXPECTED = "/auth/api-docs"

    String CLOSED_INCOMING_URL_3 = "any/profile/profile/api-docs"
    String CLOSED_INCOMING_URL_3_EXPECTED = "/profile/api-docs"

    String CLOSED_INCOMING_URL_4 = "any/auth/auth/api-docs/"
    String CLOSED_INCOMING_URL_4_EXPECTED = "/auth/api-docs/"

    String CLOSED_INCOMING_URL_5 = "any/auth/auth/api-docs/any"
    String CLOSED_INCOMING_URL_5_EXPECTED = "/auth/api-docs/any"

    String CLOSED_INCOMING_URL_6 = "any/auth/auth/api-DOCS/any/string"
    String CLOSED_INCOMING_URL_6_EXPECTED = "/auth/api-docs/any/string"

    String CLOSED_INCOMING_URL_7 = "ANY/auth/auth/api-docs/any?arg1=any-string&agr2=1000"
    String CLOSED_INCOMING_URL_7_EXPECTED = "/auth/api-docs/any"

    String CLOSED_INCOMING_URL_8 = "any/AUTH/auth/api-docs/any?arg1=admin"
    String CLOSED_INCOMING_URL_8_EXPECTED = "/auth/api-docs/any"

    String CLOSED_INCOMING_URL_20 = "any/auth/auth/api-docs/internal"
    String CLOSED_INCOMING_URL_20_EXPECTED = "/auth/api-docs/internal"

    String CLOSED_INCOMING_URL_21 = "any/auth/auth/api-docs/actuator/any?arg1=any-string&agr2=1000"
    String CLOSED_INCOMING_URL_21_EXPECTED = "/auth/api-docs/actuator/any"

    String CLOSED_INCOMING_URL_22 = "ANY/auth/auth/api-docs/any/ADMIN?arg1=any-string&agr2=1000"
    String CLOSED_INCOMING_URL_22_EXPECTED = "/auth/api-docs/any/admin"

}
