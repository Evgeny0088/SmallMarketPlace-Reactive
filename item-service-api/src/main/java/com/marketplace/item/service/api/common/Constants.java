package com.marketplace.item.service.api.common;

public interface Constants {

    /*
    headers
     */
    String HEADER_PROFILE_ID = "Profile-Id";
    String ROLES = "Roles";
    
    /*
    brand entity fields
    */
    String ID = "id";
    String BRAND_TABLE_NAME = "brands";
    String BRAND_NAME = "brand_name";
    String OWNER_ID = "owner_id";
    String BRAND_ID = "brand_id";
    String CREATED_AT = "created_at";
    String UPDATED_AT = "updated_at";
    String CHILDREN = "children";
    
    /*
    item entity fields
    */
    String ITEM_TABLE_NAME = "items";
    String PARENT_ITEM = "parent_item";
    String ITEM_TYPE = "item_type";
    String ITEMS_COUNT = "items_count";

    /*
    sell transactions entity
    */
    String TRANSACTIONS_TABLE_NAME = "sell_transactions";

    /*
    query binding and mapping
    */
    String LIMIT = "limit";
    String BRAND_CREATED_AT = "brand_created_at";
    String BRAND_UPDATED_AT = "brand_updated_at";
    String ITEM_CREATED_AT = "item_created_at";
    String ITEM_ID = "item_id";
    String UPDATED_NAME_FIELD = "updatedName";
    long NULLABLE_ID = -1L;
    
    /*
    end points
    */
    String API_PREFIX_BRAND = "/api/v1/brands";
    String GET_ALL_BRANDS = "/get-brands";
    String DELETE = "/delete";
    String REQUEST_FOR_SELL = "/request-for-sell";
    String API_PREFIX_ITEMS = "/api/v1/items";
    
    /*
    sort by time in pagination
    */
    String DESC = "DESC";
    String ASC = "ASC";

    /*
    kafka
    */
    String ITEM_TRANSACTION_REQUEST_TOPIC = "item-transaction-request";
    String ITEM_TRANSACTION_RESPONSE_TOPIC = "item-transaction-response";

    /*
    redis
    */
    String ITEMS_COLLECTION_SET = "ITEM_DETAILS_SET";
    String REDIS_DELIMITER = "__";

}
