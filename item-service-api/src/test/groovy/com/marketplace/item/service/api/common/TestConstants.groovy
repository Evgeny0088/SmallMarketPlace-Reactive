package com.marketplace.item.service.api.common

interface TestConstants {

    String PROFILE_1 = "profile-1"
    String PROFILE_2 = "profile-2"
    String BR_NAME_1 = "nike"
    String BR_NAME_2 = "updated"
    String EMPTY_JSON = "{}"

    /*
    brand requests
    */
    String BRAND_CREATE_REQ_1 = "requests/brand/create_brand_1.json"
    String BRAND_CREATE_REQ_2 = "requests/brand/create_brand_2.json"
    String BRAND_CREATE_REQ_3 = "requests/brand/create_brand_3.json"
    String BRAND_CREATE_REQ_4 = "requests/brand/create_brand_4.json"
    String BRAND_CREATE_REQ_5 = "requests/brand/create_brand_5.json"

    /*
    item requests
    */
    String ITEM_CREATE_REQ_1 = "requests/item/create/create_item_1.json"
    String ITEM_CREATE_REQ_2 = "requests/item/create/create_item_2.json"
    String ITEM_CREATE_REQ_3 = "requests/item/create/create_item_3.json"
    String ITEM_CREATE_REQ_4 = "requests/item/create/create_item_4.json"
    String ITEM_CREATE_REQ_5 = "requests/item/create/create_item_5.json"
    String ITEM_CREATE_REQ_6 = "requests/item/create/create_item_6.json"
    String ITEM_CREATE_REQ_7 = "requests/item/create/create_item_7.json"

    String ITEM_DELETE_REQ_1 = "requests/item/delete/delete_item_1.json"
    String ITEM_DELETE_REQ_2 = "requests/item/delete/delete_item_2.json"
    String ITEM_DELETE_REQ_3 = "requests/item/delete/delete_item_3.json"
    String ITEM_DELETE_REQ_4 = "requests/item/delete/delete_item_4.json"

    String ITEM_SELL_REQ_1 = "requests/sellItems/sell_item_1.json"
    String ITEM_SELL_REQ_2 = "requests/sellItems/sell_item_2.json"
    String ITEM_SELL_REQ_3 = "requests/sellItems/sell_item_3.json"

    String INIT_QUERY_1 = "insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);"
    String INIT_QUERY_2 =
            """insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);
insert into items(id, brand_id, item_type, created_at) values(100, 100, 'PACK', current_timestamp);"""

    String INIT_QUERY_3 =
            """insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);
insert into items(id, brand_id, item_type, created_at) values(100, 100, 'PACK', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(101, 100, 100, 'ITEM', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(102, 100, 100, 'ITEM', current_timestamp);"""

    String INIT_QUERY_4 =
            """insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);
insert into brands(id, owner_id, brand_name, created_at) values (101, 'profile-1', 'nike-1', current_timestamp);
insert into items(id, brand_id, item_type, created_at) values(100, 100, 'PACK', current_timestamp);"""

    String INIT_QUERY_5 =
            """insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);
insert into items(id, brand_id, item_type, created_at) values(100, 100, 'PACK', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(101, 100, 100, 'ITEM', current_timestamp);"""

    String INIT_QUERY_6 =
            """insert into brands(id, owner_id, brand_name, created_at) values (100, 'profile-1', 'nike', current_timestamp);
insert into items(id, brand_id, item_type, created_at) values(100, 100, 'PACK', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(101, 100, 100, 'ITEM', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(102, 100, 100, 'ITEM', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(103, 100, 100, 'PACK', current_timestamp);
insert into items(id, brand_id, parent_item, item_type, created_at) values(104, 100, 100, 'ITEM', current_timestamp);"""

    /*
    brand responses
    */
    String BRAND_CREATE_RESP_1 = "responses/brand/create_brand_1.json"
    String SAVED_BRAND_1 = "responses/brand/saved_brand_1.json"
    String SAVED_BRAND_2 = "responses/brand/saved_brand_2.json"
    String SAVED_BRAND_3 = "responses/brand/saved_brand_3.json"

    String UPDATE_BRAND_1 = "responses/brand/update_brand_1.json"
    String DELETE_BRAND_1 = "responses/brand/delete_brand_1.json"

    /*
    item responses
    */
    String ITEM_CREATE_RESP_1 = "responses/item/create/create_item_1.json"
    String ITEM_CREATE_RESP_2 = "responses/item/create/create_item_2.json"
    String ITEM_CREATE_RESP_3 = "responses/item/create/create_item_3.json"

    String ITEM_DELETE_RESP_1 = "responses/item/delete/delete_item_1.json"
    String ITEM_DELETE_RESP_2 = "responses/item/delete/delete_item_2.json"
    String ITEM_DELETE_RESP_3 = "responses/item/delete/delete_item_3.json"

    String ITEM_SOLD_RESP_1 = "responses/item/sellItems/sold_item_1.json"
    String ITEM_SOLD_RESP_2 = "responses/item/sellItems/sold_item_2.json"

    /*
    content in redis
    */
    String ITEM_IN_REDIS_1 = "responses/item/redis/item_in_redis_1.json"
    String ITEM_IN_REDIS_2 = "responses/item/redis/item_in_redis_2.json"
    String ITEM_IN_REDIS_3 = "responses/item/redis/item_in_redis_3.json"

    /*
    ignored fields
    */
    String CREATED_AT_IGNORE = "createdAt"
    String UPDATE_AT_IGNORE = "updatedAt"
    String MESSAGE_IGNORE = "message"
}