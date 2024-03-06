package com.marketplace.sale.service.api.controller

import com.marketplace.sale.service.api.BaseSpecification
import com.marketplace.sale.service.api.common.Constants
import com.marketplace.sale.service.api.common.TestConstants
import com.marketplace.sale.service.api.dto.ItemDetails
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Shared

import java.nio.ByteBuffer
import java.util.function.Consumer

import static com.marketplace.sale.service.api.data.TestData.pageableRequest_0
import static com.marketplace.sale.service.api.data.TestData.pageableRequest_1
import static com.marketplace.sale.service.api.data.TestData.savedItems_1
import static com.marketplace.sale.service.api.data.TestData.sellRequest_1
import static com.marketplace.sale.service.api.utils.SaleOrdersUtils.createKeyFromString
import static com.marketplace.sale.service.api.utils.VerificationUtils.retrieveObject
import static com.marketplace.sale.service.api.utils.VerificationUtils.verifyJsonResponse
import static com.marketplace.sale.service.api.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.sale.service.api.utils.WebClientFactory.headersSetup
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString
import static com.marketplace.test.helper.starter.utils.HelperUtils.wireMockStubs

class SaleControllerTest extends BaseSpecification implements TestConstants, Constants {

    @Shared
    static Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)

    def "get items page" () {
        given:
        String path = baseUrl() + SALE_ROOT_PATH + GET_ITEMS_PAGE
        Object expBody = retrieveObject(mapper, expected, Object)
         savedInRedis.forEach (it-> saveToRedis(it))

        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, path, headers, input)

        expect:
        verifyJsonResponse(mapper, result, expBody, expStatus)

        where:
        input                                | savedInRedis          | expStatus          | expected
        pageableRequest_1() | savedItems_1()       | HttpStatus.OK | PAGE_RESPONSE_RESP_1
        pageableRequest_0() | savedItems_1()       | HttpStatus.BAD_REQUEST | null

    }

    def "sell item" () {
        given:
        long brandId = input.getBrandId()
        long itemId = input.getItemId()
        long itemCount = input.getItemCount()
        String path = baseUrl() + SALE_ROOT_PATH + "/sell/$brandId/$itemId?count=$itemCount"
        Object expBody = retrieveObject(mapper, expected, Object)
        wireMockStubs(readFileAsString(itemResponse), ITEMS_PATH_SELL_REQUEST, HttpStatus.OK, HttpMethod.POST)
        savedInRedis.forEach (it-> saveToRedis(it))

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, path, headers, null)

        then:
        verifyJsonResponse(mapper, result, expBody, expStatus)

        where:
        input                     | itemResponse              | savedInRedis          | expStatus          | expected
        sellRequest_1()  | ITEM_SALE_1_FEIGN  | savedItems_1()       | HttpStatus.OK | SALE_RESPONSE_1
    }

    private void saveToRedis(ItemDetails details) {
        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(details.getBrandId()))
        redisRepoForTest.appendToCollection(byteKey, 1.0d, details).block()
    }
}
