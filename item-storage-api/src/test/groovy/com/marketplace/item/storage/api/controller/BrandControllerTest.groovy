package com.marketplace.item.storage.api.controller

import com.marketplace.item.storage.api.BaseSpecification
import com.marketplace.item.storage.api.common.Constants
import com.marketplace.item.storage.api.common.TestConstants
import com.marketplace.item.storage.api.dto.BrandRequest
import com.marketplace.item.storage.api.dto.BrandResponse
import com.marketplace.item.storage.api.dto.ItemDetails
import com.marketplace.item.storage.api.entity.Brand
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext

import java.nio.ByteBuffer
import java.util.function.Consumer

import static com.marketplace.item.storage.api.data.TestData.brand_req_1
import static com.marketplace.item.storage.api.data.TestData.brand_req_2
import static com.marketplace.item.storage.api.data.TestData.details_1
import static com.marketplace.item.storage.api.utils.ItemServiceUtils.createKeyFromString
import static com.marketplace.item.storage.api.utils.VerificationUtils.verifyBrandsInDb
import static com.marketplace.item.storage.api.utils.VerificationUtils.verifyExistenceInRedis
import static com.marketplace.item.storage.api.utils.VerificationUtils.verifyJsonResponse
import static com.marketplace.item.storage.api.utils.WebClientFactory.callServiceForResponse
import static com.marketplace.item.storage.api.utils.WebClientFactory.headersSetup
import static com.marketplace.test.helper.starter.utils.HelperUtils.readFileAsString
import static com.marketplace.item.storage.api.utils.VerificationUtils.retrieveObject

@DirtiesContext
class BrandControllerTest extends BaseSpecification implements Constants, TestConstants {

    static rootPath

    def setup() {
        rootPath = baseUrl() + API_PREFIX_BRAND
        brandRepository.deleteAll().block()
        redisRepository.flushAll().block()
    }

    def "create brand" () {
        given:
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, PROFILE_1, null)
        BrandRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), BrandRequest) : null
        Object expBody = retrieveObject(mapper, expected, Object)

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, rootPath, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expBody, expStatus, CREATED_AT_IGNORE, ID)

        where:
        request                                 | expected                                | expStatus
        BRAND_CREATE_REQ_1 | BRAND_CREATE_RESP_1 | HttpStatus.OK
        BRAND_CREATE_REQ_2 | BRAND_CREATE_RESP_1 | HttpStatus.OK
        BRAND_CREATE_REQ_3 | null                                           | HttpStatus.BAD_REQUEST
    }

    def "update brand" () {
        given:
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, profileId, null)
        BrandRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), BrandRequest) : null
        BrandResponse expBody = retrieveObject(mapper, expected, BrandResponse)
        Brand expBodyInDB = retrieveObject(mapper, expInDB, Brand)

        if (savedInDB != null) {
            Brand saved = brandRepository.saveBrandOnRequest(savedInDB, PROFILE_1).block()
            if (expBody != null) expBody.setId(saved.getId())
            if (expBodyInDB != null) expBodyInDB.setId(saved.getId())
        }

        when:
        def result = callServiceForResponse(HttpMethod.PUT, expStatus, webTestClient, rootPath, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expBody, expStatus, UPDATE_AT_IGNORE, MESSAGE_IGNORE)
        verifyBrandsInDb(brandRepository, mapper, expBodyInDB, CREATED_AT_IGNORE, UPDATE_AT_IGNORE)

        where:
        request                                 | profileId      | savedInDB        | expInDB                    | expected                                | expStatus
        BRAND_CREATE_REQ_1 | PROFILE_1  | brand_req_1() | SAVED_BRAND_1   | null                                            | HttpStatus.BAD_REQUEST
        BRAND_CREATE_REQ_3 | PROFILE_1  | brand_req_1() | SAVED_BRAND_1   | null                                            | HttpStatus.BAD_REQUEST
        BRAND_CREATE_REQ_4 | PROFILE_1  | null                      | null                              | null                                            | HttpStatus.BAD_REQUEST
        BRAND_CREATE_REQ_5 | PROFILE_2  | brand_req_1() | SAVED_BRAND_1   | null                                            | HttpStatus.FORBIDDEN
        BRAND_CREATE_REQ_5 | PROFILE_1  | null                       | null                             | null                                            | HttpStatus.NOT_FOUND
        BRAND_CREATE_REQ_5 | PROFILE_1  | brand_req_1() | SAVED_BRAND_2   | UPDATE_BRAND_1             | HttpStatus.OK
    }

    def "delete brand and cleanup cache" () {
        given:
        String path = rootPath + DELETE
        Consumer<HttpHeaders> headers = headersSetup(MediaType.APPLICATION_JSON, profileId, null)
        BrandRequest requestBody = request != null ? mapper.readValue(readFileAsString(request), BrandRequest) : null
        BrandResponse expResponse = retrieveObject(mapper, expected, BrandResponse)
        Brand expBodyInDB = retrieveObject(mapper, expInDB, Brand)
        ItemDetails details = details_1()

        if (savedInDB != null) {
            Brand saved = brandRepository.saveBrandOnRequest(savedInDB, PROFILE_1).block()
            details.setBrandId(saved.getId())
            if (expBodyInDB != null) expBodyInDB.setId(saved.getId())
        }
        saveToRedis(details)

        when:
        def result = callServiceForResponse(HttpMethod.POST, expStatus, webTestClient, path, headers, requestBody)

        then:
        verifyJsonResponse(mapper, result, expResponse, expStatus)
        verifyBrandsInDb(brandRepository, mapper, expBodyInDB, CREATED_AT_IGNORE)
        verifyExistenceInRedis(redisRepository, expInCache)

        where:
        request                                 | profileId      | savedInDB        | expInCache    | expInDB                    | expected                    | expStatus
        BRAND_CREATE_REQ_1 | PROFILE_1  | brand_req_1() | false                  | null                              | DELETE_BRAND_1  | HttpStatus.OK
        BRAND_CREATE_REQ_1 | PROFILE_1  | brand_req_2() | true                   | SAVED_BRAND_3   | null                               | HttpStatus.NOT_FOUND
        BRAND_CREATE_REQ_1 | OWNER_ID  | brand_req_1() | true                   | null                              | null                               | HttpStatus.NOT_FOUND
    }

    private void saveToRedis(ItemDetails details) {
        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(details.getBrandId()))
        redisRepository.appendToCollection(byteKey, 1.0d, details).block()
    }
}
