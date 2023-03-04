package com.marketplace.authservice.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.MessageConstants
import com.marketplace.ProfileDto
import com.marketplace.authservice.common.Constants
import com.marketplace.authservice.common.TestConstants
import com.marketplace.authservice.dto.ResponseDto
import com.marketplace.exception.lib.exception.CustomExceptionDto
import com.marketplace.jwt.dto.JwtDto
import org.apache.logging.log4j.util.Strings
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.EntityExchangeResult

import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString

class TestVerificationUtils implements TestConstants, Constants, MessageConstants {

    static void verifyNewProfileResponse(EntityExchangeResult<byte[]> response,
                                         String expResponse,
                                         String expMessage,
                                         HttpStatus expStatus,
                                         String[] expRoles,
                                         ObjectMapper objectMapper, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<ProfileDto> returnedDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            ProfileDto profileDto = objectMapper.convertValue(returnedDto.getContent(), ProfileDto)
            assert returnedDto.getMessage() == expMessage
            assert expRoles.stream().allMatch((role)-> profileDto.getRoles().contains(role))
            assert !profileDto.isActive()
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyLoginResponse(EntityExchangeResult<byte[]> response,
                                         String expResponse,
                                         String expMessage,
                                         HttpStatus expStatus,
                                         ObjectMapper objectMapper, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<JwtDto> returnedDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            assert returnedDto.getMessage() == expMessage
            assert Strings.isNotBlank(returnedDto.getContent().getAt(ACCESS_TOKEN).toString())
        }
        else {
            verifyJsonResponse(objectMapper, response, expResponse, ignoredFields)
        }
    }

    static void verifyRequestEmailResponse(EntityExchangeResult<byte[]> response,
                                           String expResponse,
                                           String expMessage,
                                           HttpStatus expStatus,
                                           ObjectMapper objectMapper, String ... ignoredFields) {
        if (expStatus == HttpStatus.OK) {
            ResponseDto<String> returnedDto = objectMapper.readValue(response.getResponseBody(), ResponseDto)
            assert returnedDto.getMessage() == expMessage
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

}
