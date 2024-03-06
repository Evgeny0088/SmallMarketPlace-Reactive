package com.marketplace.item.storage.api.controller

import com.marketplace.item.storage.api.BaseSpecification
import com.marketplace.item.storage.api.common.Constants
import com.marketplace.item.storage.api.common.TestConstants
import com.marketplace.item.storage.api.dto.ItemDetails
import com.marketplace.item.storage.api.dto.ItemRequest
import com.marketplace.item.storage.api.dto.ItemResponse
import com.marketplace.item.storage.api.dto.ItemTransactionRequest
import com.marketplace.item.storage.api.dto.ItemTransactionResponse
import com.marketplace.item.storage.api.dto.ItemUpdatePipeline
import com.marketplace.item.storage.api.dto.ItemUpdateRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext

import java.util.function.Consumer

import static com.marketplace.item.storage.api.utils.VerificationUtils.*
import static com.marketplace.item.storage.api.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.item.storage.api.utils.WebClientFactory.headersSetup
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString

@DirtiesContext
class ItemControllerTest extends BaseSpecification implements Constants, TestConstants {

    static rootPath

    def setup() {
        rootPath = baseUrl() + API_PREFIX_ITEMS
        brandRepository.deleteAll().block()
        redisRepository.flushAll().block()
        transactionRepository.deleteAll().block()
    }

    def "create item" () {
        given:
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)
        ItemRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), ItemRequest) : null
        ItemDetails expItemDetails = retrieveObject(mapper, expInRedis, ItemDetails)
        ItemResponse expResponse = retrieveObject(mapper, expected, ItemResponse)
        ItemDetails itemDetails = retrieveObject(mapper, savedInRedis, ItemDetails)

        if (query != null) databaseClient.sql(query).then().block()
        loadIntoRedis(itemDetails)

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, rootPath, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expResponse, expStatus, CREATED_AT_IGNORE, ID)
        verifyItemsInDb(itemRepository, mapper, result.getResponseBody(), expInDB)
        verifyContentInRedis(
                redisRepository,
                mapper,
                100,
                expItemDetails)

        where:
        request                                 | savedInRedis          | query                   | expInDB | expInRedis              | expected                                | expStatus
        ITEM_CREATE_REQ_1     | null                             | INIT_QUERY_1 | true          | null                              | ITEM_CREATE_RESP_1     | HttpStatus.OK
        ITEM_CREATE_REQ_2     | null                             | INIT_QUERY_2 | true          | null                              | ITEM_CREATE_RESP_2     | HttpStatus.OK
        ITEM_CREATE_REQ_3     | null                             | INIT_QUERY_2 | true          | ITEM_IN_REDIS_1  | ITEM_CREATE_RESP_3     | HttpStatus.OK
        ITEM_CREATE_REQ_3     | ITEM_IN_REDIS_2 | INIT_QUERY_3 | true          | ITEM_IN_REDIS_3  | ITEM_CREATE_RESP_3     | HttpStatus.OK

        ITEM_CREATE_REQ_4     | null                             | INIT_QUERY_2 | false         | null                              | null                                           | HttpStatus.BAD_REQUEST
        ITEM_CREATE_REQ_4     | ITEM_IN_REDIS_2 | INIT_QUERY_3 | false         | ITEM_IN_REDIS_2  | null                                           | HttpStatus.BAD_REQUEST
        ITEM_CREATE_REQ_5     | ITEM_IN_REDIS_2 | INIT_QUERY_3 | false         | ITEM_IN_REDIS_2  | null                                           | HttpStatus.BAD_REQUEST
        ITEM_CREATE_REQ_6     | null                             | INIT_QUERY_4 | false         | null                              | null                                           | HttpStatus.BAD_REQUEST
        ITEM_CREATE_REQ_7     | ITEM_IN_REDIS_2 | INIT_QUERY_3 | false         | ITEM_IN_REDIS_2  | null                                           | HttpStatus.BAD_REQUEST
    }

    def "delete item" () {
        given:
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, profileId, null)
        ItemUpdateRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), ItemUpdateRequest) : null
        ItemDetails expItemDetails = retrieveObject(mapper, expInRedis, ItemDetails)
        ItemResponse expResponse = retrieveObject(mapper, expected, ItemResponse)
        ItemDetails itemDetails = retrieveObject(mapper, savedInRedis, ItemDetails)
        String path = rootPath + DELETE

        if (query != null) databaseClient.sql(query).then().block()
        loadIntoRedis(itemDetails)

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, path, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expResponse, expStatus, CREATED_AT_IGNORE, ID)
        verifyItemsInDb(itemRepository, mapper, result.getResponseBody(), false)
        verifyContentInRedis(
                redisRepository,
                mapper,
                100,
                expItemDetails)

        where:
        request                                 | profileId     | savedInRedis          | query                   | expInRedis               | expected                                | expStatus
        ITEM_DELETE_REQ_1      | PROFILE_1 | null                             | INIT_QUERY_2  | null                              | ITEM_DELETE_RESP_1      | HttpStatus.OK
        ITEM_DELETE_REQ_1      | PROFILE_1 | ITEM_IN_REDIS_2 | INIT_QUERY_3  | null                              | ITEM_DELETE_RESP_1      | HttpStatus.OK
        ITEM_DELETE_REQ_3      | PROFILE_1 | ITEM_IN_REDIS_2 | INIT_QUERY_3  | ITEM_IN_REDIS_1  | ITEM_DELETE_RESP_2      | HttpStatus.OK
        ITEM_DELETE_REQ_4      | PROFILE_1 | ITEM_IN_REDIS_2 | INIT_QUERY_3  | ITEM_IN_REDIS_1  | ITEM_DELETE_RESP_3      | HttpStatus.OK
        ITEM_DELETE_REQ_3      | PROFILE_1 | ITEM_IN_REDIS_1 | INIT_QUERY_5  | null                              | ITEM_DELETE_RESP_2      | HttpStatus.OK

        ITEM_DELETE_REQ_2      | PROFILE_1 | ITEM_IN_REDIS_2 | INIT_QUERY_3  | ITEM_IN_REDIS_2  | null                                            | HttpStatus.NOT_FOUND
        ITEM_DELETE_REQ_1      | OWNER_ID | ITEM_IN_REDIS_2 | INIT_QUERY_3  | ITEM_IN_REDIS_2  | null                                            | HttpStatus.FORBIDDEN

    }

    def "sell item" () {
        given:
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, null, null)
        ItemTransactionRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), ItemTransactionRequest) : null
        ItemDetails expItemDetails = retrieveObject(mapper, expInRedis, ItemDetails)
        ItemTransactionResponse expResponse = retrieveObject(mapper, expected, ItemTransactionResponse)
        ItemDetails itemDetails = retrieveObject(mapper, savedInRedis, ItemDetails)
        String path = rootPath + REQUEST_FOR_SELL

        if (query != null) databaseClient.sql(query).then().block()
        loadIntoRedis(itemDetails)

        if (savedTransaction) {
            transactionRepository.saveNewTransaction(requestBody).block()
        }

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, path, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expResponse, expStatus)
        verifyContentInRedis(redisRepository, mapper, 100, expItemDetails)

        where:
        request                           | savedTransaction | savedInRedis          | query                   | expInRedis                | expected                               | expStatus
        ITEM_SELL_REQ_1      | false                           | null                             | INIT_QUERY_2  | null                              | null                                           | HttpStatus.NOT_FOUND
        ITEM_SELL_REQ_2      | false                           | null                             | INIT_QUERY_3  | ITEM_IN_REDIS_1  | ITEM_SOLD_RESP_1          | HttpStatus.OK
        ITEM_SELL_REQ_2      | false                           | ITEM_IN_REDIS_2 | INIT_QUERY_3  | ITEM_IN_REDIS_1  | ITEM_SOLD_RESP_1          | HttpStatus.OK
        ITEM_SELL_REQ_3      | false                           | ITEM_IN_REDIS_3 | INIT_QUERY_6  | ITEM_IN_REDIS_1  | ITEM_SOLD_RESP_2          | HttpStatus.OK
        ITEM_SELL_REQ_3      | false                           | ITEM_IN_REDIS_1 | INIT_QUERY_5  | ITEM_IN_REDIS_1  | null                                           | HttpStatus.BAD_REQUEST
        ITEM_SELL_REQ_3      | false                           | ITEM_IN_REDIS_1 | INIT_QUERY_2  | ITEM_IN_REDIS_1  | null                                           | HttpStatus.BAD_REQUEST
        ITEM_SELL_REQ_3      | true                            | ITEM_IN_REDIS_3 | INIT_QUERY_6  | ITEM_IN_REDIS_3  | null                                           | HttpStatus.BAD_REQUEST
    }

    void loadIntoRedis(ItemDetails details) {
        if (details != null) {
            ItemUpdatePipeline pipeline = ItemUpdatePipeline.builder().itemDetails(details).build()
            redisRepository.invalidateCacheOnItemUpdate(pipeline).block()
        }
    }
}
