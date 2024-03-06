package com.marketplace.sale.service.api.utils;

import com.marketplace.sale.service.api.common.Constants;
import java.nio.ByteBuffer;

public class SaleOrdersUtils implements Constants {

    public static ByteBuffer createKeyFromString(String prefix, String value) {
        return ByteBuffer.wrap(prefix.concat(REDIS_DELIMITER).concat(value).getBytes());
    }
}
