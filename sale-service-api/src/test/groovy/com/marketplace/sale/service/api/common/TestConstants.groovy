package com.marketplace.sale.service.api.common

interface TestConstants {

    long BRAND_ID = 100
    long BRAND_ID_1 = 101
    String HEADER_PROFILE_ID = "Profile-Id"
    String ROLES = "roles"
    String PROFILE_1 = "profile-1"
    String PROFILE_2 = "profile-2"
    String BR_NAME_1 = "nike"
    String BR_NAME_2 = "updated"
    String EMPTY_JSON = "{}"

    /*
    item responses
    */
    String ITEM_SALE_1_FEIGN = "responses/item_store_feign_200.json"
    String SALE_RESPONSE_1 = "responses/sale_response_1.json"

    /*
    sale responses
    */
    String PAGE_RESPONSE_RESP_1 = "responses/page_response_1.json"


    /*
    ignored fields
    */
    String CREATED_AT_IGNORE = "createdAt"
    String UPDATE_AT_IGNORE = "updatedAt"
    String MESSAGE_IGNORE = "message"

}