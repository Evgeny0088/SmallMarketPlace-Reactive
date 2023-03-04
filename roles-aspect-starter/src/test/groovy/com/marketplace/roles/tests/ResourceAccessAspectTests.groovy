package com.marketplace.roles.tests

import com.marketplace.roles.BaseSpecification
import com.marketplace.roles.common.ApplicationConstants
import com.marketplace.roles.common.Constants
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Stepwise

import static com.marketplace.roles.utils.WebClientFactory.*
import static com.marketplace.roles.utils.HelpTestMethods.*

@Stepwise
class ResourceAccessAspectTests extends BaseSpecification implements ApplicationConstants, Constants {

    def "check if endpoint is being called by owner and return OK status"(){
        given:
        Map<String, String> headerFields = new HashMap<>()
        Object requestBody = requestJson != null ? mapper.readValue(readFileAsString(requestJson), Object) : null
        headerFields.put(PROFILE_ID, CORRECT_OWNER_ID)

        if (withExtraHeader) {
            headerFields.put(EXTRA_HEADER, UUID.randomUUID().toString())
        }

        expect:
        callServiceForResponse(httpMethod, expectedStatus, webTestClient, endPoint, CORRECT_OWNER_ID, headerFields, filePath, requestBody)

        where:
        endPoint        | httpMethod        | withExtraHeader | filePath    | requestJson     | expectedStatus
        TEST_ENDPOINT_1 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_2 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_3 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_4 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_5 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_6 | HttpMethod.GET    | true            | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_7 | HttpMethod.GET    | true            | null        | null            | HttpStatus.OK
        TEST_ENDPOINT_8 | HttpMethod.GET    | false           | null        | null            | HttpStatus.OK

        TEST_ENDPOINT_9 | HttpMethod.POST   | false        | null           | REQUEST_BODY_1  | HttpStatus.OK
        TEST_ENDPOINT_10 | HttpMethod.POST  | false        | null           | REQUEST_BODY_2  | HttpStatus.OK
        TEST_ENDPOINT_11 | HttpMethod.POST  | true         | null           | REQUEST_BODY_2  | HttpStatus.OK
        TEST_ENDPOINT_12 | HttpMethod.POST  | true         | null           | REQUEST_BODY_3  | HttpStatus.OK
        TEST_ENDPOINT_13 | HttpMethod.POST  | false        | null           | REQUEST_BODY_4  | HttpStatus.OK
        TEST_ENDPOINT_14 | HttpMethod.POST  | true         | null           | REQUEST_BODY_5  | HttpStatus.OK
        TEST_ENDPOINT_15 | HttpMethod.PATCH | false        | null           | REQUEST_BODY_6  | HttpStatus.OK
        TEST_ENDPOINT_16 | HttpMethod.PUT   | false        | null           | REQUEST_BODY_7  | HttpStatus.OK
        TEST_ENDPOINT_17 | HttpMethod.POST  | false        | REQUEST_PART_1 | null            | HttpStatus.OK
        TEST_ENDPOINT_18 | HttpMethod.POST  | true         | REQUEST_PART_2 | null            | HttpStatus.OK
        TEST_ENDPOINT_19 | HttpMethod.POST  | true         | REQUEST_PART_1 | null            | HttpStatus.OK
    }

    def "check if endpoint is being called not by owner and return 400 status"() {
        given:
        Map<String, String> headerFields = new HashMap<>()
        Object requestBody = requestJson != null ? mapper.readValue(readFileAsString(requestJson), Object) : null
        if (withHeader) headerFields.put(PROFILE_ID, wrongId ? UUID.randomUUID().toString() : CORRECT_OWNER_ID)

        expect:
        callServiceForResponse(httpMethod, expectedStatus, webTestClient, endPoint,
                                                                    wrongId ? UUID.randomUUID().toString() : CORRECT_OWNER_ID,
                                                                        headerFields, filePath, requestBody)

        where:
        endPoint         | httpMethod      | withHeader | wrongId | filePath       | requestJson     | expectedStatus
        TEST_ENDPOINT_20 | HttpMethod.GET  | true       | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_21 | HttpMethod.GET  | false      | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_22 | HttpMethod.GET  | false      | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_23 | HttpMethod.GET  | true       | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_24 | HttpMethod.GET  | true       | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_25 | HttpMethod.GET  | true       | false   | null           | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_26 | HttpMethod.GET  | true       | true    | null           | null            | HttpStatus.BAD_REQUEST

        TEST_ENDPOINT_27 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_8  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_28 | HttpMethod.POST | true       | true    | null           | REQUEST_BODY_2  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_29 | HttpMethod.POST | true       | true    | null           | REQUEST_BODY_2  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_30 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_2  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_31 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_9  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_32 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_1  | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_33 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_10 | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_34 | HttpMethod.POST | true       | false   | null           | REQUEST_BODY_11 | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_35 | HttpMethod.POST | true       | false   | REQUEST_PART_1 | null            | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_36 | HttpMethod.POST | true       | true    | REQUEST_PART_2 | null            | HttpStatus.BAD_REQUEST
    }

    def "verify roles in different cases"() {
        given:
        Map<String, String> headerFields = new HashMap<>()
        headerFields.put(PROFILE_ID, CORRECT_OWNER_ID)
        if (specifiedRoles != null)
            headerFields.put(ROLES, specifiedRoles)

        expect:
        callServiceForResponse(httpMethod, expectedStatus, webTestClient, endPoint, CORRECT_OWNER_ID, headerFields, null, null)

        where:
        endPoint         | httpMethod     | specifiedRoles | expectedStatus
        TEST_ENDPOINT_37 | HttpMethod.GET | ROLES_1        | HttpStatus.OK
        TEST_ENDPOINT_38 | HttpMethod.GET | ROLES_1        | HttpStatus.OK
        TEST_ENDPOINT_39 | HttpMethod.GET | ROLES_2        | HttpStatus.OK
        TEST_ENDPOINT_40 | HttpMethod.GET | ROLES_2        | HttpStatus.OK
        TEST_ENDPOINT_41 | HttpMethod.GET | ROLES_3        | HttpStatus.OK
        TEST_ENDPOINT_42 | HttpMethod.GET | ROLES_3        | HttpStatus.OK
        TEST_ENDPOINT_43 | HttpMethod.GET | ROLES_4        | HttpStatus.OK
        TEST_ENDPOINT_44 | HttpMethod.GET | ROLES_4        | HttpStatus.OK
        TEST_ENDPOINT_45 | HttpMethod.GET | ROLES_1        | HttpStatus.OK
        TEST_ENDPOINT_46 | HttpMethod.GET | ROLES_5        | HttpStatus.OK
        TEST_ENDPOINT_47 | HttpMethod.GET | ROLES_6        | HttpStatus.OK
        TEST_ENDPOINT_50 | HttpMethod.GET | ROLES_9        | HttpStatus.OK

        TEST_ENDPOINT_51 | HttpMethod.GET | L_CASE_ROLE    | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_48 | HttpMethod.GET | ROLES_7        | HttpStatus.FORBIDDEN
        TEST_ENDPOINT_49 | HttpMethod.GET | ROLES_8        | HttpStatus.FORBIDDEN
        TEST_ENDPOINT_52 | HttpMethod.GET | ROLES_2        | HttpStatus.FORBIDDEN
        TEST_ENDPOINT_53 | HttpMethod.GET | ROLES_EMPTY    | HttpStatus.FORBIDDEN
    }
}