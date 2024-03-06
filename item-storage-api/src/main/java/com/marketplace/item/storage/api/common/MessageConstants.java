package com.marketplace.item.storage.api.common;

public interface MessageConstants {

    String OBJECT_CREATED = "Object is created!";
    String OBJECT_UPDATED = "Object ( id = %s ) is just modified!";
    String OBJECT_IS_DELETED = "Object (%s = %s ) is deleted";
    String OBJECT_FOUND = "Object is found:\n%s";
    
    /*
    messages
    */
    String OBJECT_NOT_FOUND = "Object (%s = %s ) is not found.";
    String FIELD_REQUEST_ERROR = "{ %s } field must be specified at this request!";
    String ITEM_TYPE_ERROR = "Item cannot be type of { ITEM } without parent!";
    String ITEM_TYPE_ERROR_1 = "Cannot set itemType { ITEM } since current item has child items!";
    String UPDATE_ONLY_BY_OWNER = "Object can be updated only by owner, current user id = %s";
    String DELETE_ONLY_BY_OWNER = "Object must be exists and can be deleted only by owner, current user id = %s";
    String SELLING_ERROR = "Not enough items in database, input count = %s";
    String SELLING_ERROR_NOT_FOUND = "Object with item_id = %s and brand_id = %s is not found";
    String SELLING_ERROR_COUNT = "Cannot sell product: not enough items count (current count = %s; required count = %s)";
    String RECOVER_MESSAGE = "Object with ( id = %s) failed due to reason:\n%s";
    String ITEM_BRAND_MISMATCH = "Item has different brand_id = %s, then parent item { id = %s, brand_id = %s}!";
    String ITEM_TYPE_ERROR_UPDATE = "Item type cannot be changed from PACK to ITEM if current item has child items!";
    String TRANSACTION_ALREADY_PROCESSED = "Transaction ( id = %s ) already processed";
    String SOLD_SUCCESSFULLY = "items sold successfully! Sold count = %s for item = %s";
    String FAILED_RECORD_ERROR = "Failed to process record: %s, with offset: %s";
    String CONSUMER_ERROR = "Error occurred in consumer: %s, with record: %s";
}