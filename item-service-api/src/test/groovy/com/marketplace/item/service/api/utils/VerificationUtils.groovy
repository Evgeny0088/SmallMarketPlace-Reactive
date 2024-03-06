package com.marketplace.item.service.api.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.marketplace.item.service.api.common.Constants
import com.marketplace.item.service.api.common.TestConstants
import com.marketplace.item.service.api.dto.ItemDetails
import com.marketplace.item.service.api.dto.ItemResponse
import com.marketplace.item.service.api.entity.Brand
import com.marketplace.item.service.api.entity.Item
import com.marketplace.item.service.api.repository.BrandRepository
import com.marketplace.item.service.api.repository.ItemRepository
import com.marketplace.item.service.api.repository.StandAloneRepository
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

    static void verifyBrandsInDb(BrandRepository repository, ObjectMapper mapper, Object expBody, String ... ignoredFields) {
        Brand brand = expBody == null ? null : repository.findBrandByName(((Brand) expBody).getBrandName()).block()
        verifyJsonBody(mapper, brand == null ? null : mapper.writeValueAsBytes(brand), expBody, ignoredFields)
    }

    static void verifyItemsInDb(ItemRepository repository, ObjectMapper mapper, byte[] response, boolean expData) {
        if (expData) {
            ItemResponse resp = mapper.readValue(response, ItemResponse)
            Item item = repository.getTestItem(resp.getId()).block()
            assert item.getId() == resp.getId()
        }
        else {
            assert true
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

    static void verifyExistenceInRedis(StandAloneRepository redisRepository, boolean expected) {
        assert redisRepository.ifAnyExists().block() == expected
    }

    static void verifyContentInRedis(StandAloneRepository redisRepository, ObjectMapper mapper, long key, Object expected) {
        ItemDetails details = redisRepository.findItemDetails(key).block()
        verifyJsonBody(mapper, details == null ? null : mapper.writeValueAsBytes(details), expected)
    }

    static <T> T retrieveObject(ObjectMapper mapper, String expString, Class<T> type) {
        return StringUtils.isBlank(expString) ? null : mapper.readValue(readFileAsString(expString), type)
    }
}
