package com.marketplace.itemstorage.common;

public interface MessageConstants {

    String OBJECT_CREATED = "Object is created!";
    String OBJECT_UPDATED = "Object ( id = %s ) is just modified!";
    String OBJECT_IS_DELETED = "Object (%s = %s ) is deleted";
    String OBJECT_FOUND = "Object is found:\n%s";

    /*
    error messages
    */
    String OBJECT_NOT_FOUND = "Object (%s = %s ) is not found.";
    String FIELD_REQUEST_ERROR = "{ %s } field must be specified at this request!";
    String ITEM_TYPE_ERROR = "Item cannot be type of { ITEM } without parent!";
    String ITEM_TYPE_ERROR_1 = "Cannot set itemType { ITEM } since current item has child items!";

    String RECOVER_MESSAGE = "Object with ( id = %s) failed due to reason:\n%s";
    String ITEM_BRAND_MISMATCH = "Item has different brand_id = %s, then parent item { id = %s, brand_id = %s}!";
    String ITEM_TYPE_ERROR_UPDATE = "Item type cannot be changed from PACK to ITEM if current item has child items!";
}