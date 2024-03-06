package com.marketplace.sale.service.api.common;

public interface Constants {

    /*
    endpoints controller
    */
    String GET_ITEMS_PAGE = "/get-items";
    String SELL_ITEM = "/sell/{brand-id}/{id}";
    String SALE_ROOT_PATH = "/api/v1/sales";

    /*
    web client
    */
    String ITEMS_PATH_SELL_REQUEST = "/api/v1/items/request-for-sell";

    /*
    redis
    */
    String ITEMS_COLLECTION_SET = "ITEM_DETAILS_SET";
    String REDIS_DELIMITER = "__";
    String DESC = "DESC";
    String ASC = "ASC";

}
