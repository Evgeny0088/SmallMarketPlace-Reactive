package com.marketplace.logger.tests

import com.marketplace.logger.BaseSpecification
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

import static com.marketplace.logger.utils.HelpTestMethods.readFileAsString
import static com.marketplace.logger.utils.WebClientFactory.callServiceForResponse

class LoggerAspectTest extends BaseSpecification {

    def "log request and response of called endpoint"() {
        given:
        Map<String, String> headerFields = new HashMap<>()
        Object requestBody = requestJson != null ? mapper.readValue(readFileAsString(requestJson), Object) : null
        String ownerId = "CORRECT_OWNER_ID"
        if (withExtraHeader) {
            headerFields.put(EXTRA_HEADER, UUID.randomUUID().toString())
        }

        expect:
        callServiceForResponse(httpMethod, expectedStatus, webTestClient, endPoint, ownerId, headerFields, filePath, requestBody)

        where:
        endPoint         | httpMethod        | withExtraHeader | filePath       | requestJson    | expectedStatus
        TEST_ENDPOINT_1  | HttpMethod.GET    | false           | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_2  | HttpMethod.GET    | false           | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_3  | HttpMethod.POST   | false           | null           | REQUEST_BODY_1 | HttpStatus.OK
        TEST_ENDPOINT_4  | HttpMethod.POST   | false           | null           | REQUEST_BODY_2 | HttpStatus.OK
        TEST_ENDPOINT_5  | HttpMethod.POST   | false           | null           | REQUEST_BODY_3 | HttpStatus.OK
        TEST_ENDPOINT_6  | HttpMethod.PUT    | true            | null           | REQUEST_BODY_4 | HttpStatus.OK
        TEST_ENDPOINT_7  | HttpMethod.PATCH  | true            | null           | REQUEST_BODY_5 | HttpStatus.OK
        TEST_ENDPOINT_8  | HttpMethod.DELETE | true            | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_9  | HttpMethod.GET    | true            | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_10 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_11 | HttpMethod.POST   | true            | REQUEST_PART_2 | null           | HttpStatus.OK
        TEST_ENDPOINT_12 | HttpMethod.POST   | true            | REQUEST_PART_2 | null           | HttpStatus.OK
        TEST_ENDPOINT_13 | HttpMethod.POST   | false           | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_14 | HttpMethod.POST   | false           | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_15 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_16 | HttpMethod.POST   | false           | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_17 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_18 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_19 | HttpMethod.GET    | true            | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_20 | HttpMethod.GET    | true            | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_21 | HttpMethod.DELETE | true            | null           | null           | HttpStatus.OK
        TEST_ENDPOINT_22 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_23 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_24 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_25 | HttpMethod.POST   | true            | REQUEST_PART_1 | null           | HttpStatus.OK
        TEST_ENDPOINT_26 | HttpMethod.GET    | true            | null           | null           | HttpStatus.OK

        TEST_ENDPOINT_30 | HttpMethod.GET    | true            | null           | null           | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_31 | HttpMethod.GET    | true            | null           | null           | HttpStatus.BAD_REQUEST
        TEST_ENDPOINT_32 | HttpMethod.GET    | true            | null           | null           | HttpStatus.BAD_REQUEST
    }
}
