package com.marketplace.sale.service.api.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.sale.service.api.common.Constants
import com.marketplace.sale.service.api.common.TestConstants
import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.StringUtils
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.EntityExchangeResult

import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString

class VerificationUtils implements TestConstants, Constants {

    static void verifyJsonResponse (ObjectMapper objectMapper,
                                    EntityExchangeResult<byte[]> response,
                                    Object expResponse,
                                    HttpStatus expStatus,
                                    String ... ignoredFields) {
        assert response.getStatus() == expStatus
        if (response.getStatus() == HttpStatus.OK) {
            verifyJsonBody(objectMapper, response.getResponseBody(), expResponse, ignoredFields)
        }
    }

    static void verifyJsonBody(ObjectMapper objectMapper, byte[] response, Object expResponse, String ... ignoredFields) {
        String[] ignored = ignoredFields != null ? Arrays.asList(ignoredFields) : new String[0]
        Customization[] customizations =
                Arrays.stream(ignored)
                        .map(field-> new Customization(field, (f1, f2) -> true))
                        .toArray(Customization.class)
        String responseString = response == null ? EMPTY_JSON : objectMapper.writeValueAsString(objectMapper.readValue(response, Object))
        String expResponseString = ObjectUtils.isEmpty(expResponse) ? EMPTY_JSON : objectMapper.writeValueAsString(expResponse)
        CustomComparator comparator = new CustomComparator(JSONCompareMode.STRICT, customizations)
        JSONAssert.assertEquals(expResponseString, responseString, comparator)
    }

    static <T> T retrieveObject(ObjectMapper mapper, String expString, Class<T> type) {
        return StringUtils.isBlank(expString) ? null : mapper.readValue(readFileAsString(expString), type)
    }
}
