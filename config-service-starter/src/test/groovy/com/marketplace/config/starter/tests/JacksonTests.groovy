package com.marketplace.config.starter.tests

import com.marketplace.config.starter.BaseSpecification
import com.marketplace.config.starter.common.Constants
import com.marketplace.config.starter.dto.TestDto

import java.time.LocalDateTime

import static com.marketplace.config.starter.utils.HelpTestMethods.readFileAsString

class JacksonTests extends BaseSpecification implements Constants{

    def "deserialize date time" () {
        given:
        LocalDateTime time = LocalDateTime.parse("2023-04-17T06:40:45+0000", DATE_TIME_UTC_FORMAT)
        Object obj = mapper.readValue(readFileAsString(RESPONSE_1), Object)

        TestDto dto = mapper.convertValue(obj, TestDto)

        expect:
        assert true

        where:
        request   | response
        REQUEST_1 | "response"

    }

}
