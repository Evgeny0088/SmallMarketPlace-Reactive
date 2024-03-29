package com.marketplace.item.service.api.data

import com.marketplace.item.service.api.common.Constants
import com.marketplace.item.service.api.common.TestConstants
import com.marketplace.item.service.api.dto.BrandRequest
import com.marketplace.item.service.api.dto.ItemDetails

class TestData implements TestConstants, Constants {

    static BrandRequest brand_req_1() {
        return new BrandRequest().tap {
            brandName = BR_NAME_1
        }
    }

    static BrandRequest brand_req_2() {
        return new BrandRequest().tap {
            brandName = BR_NAME_2
        }
    }

    static ItemDetails details_1() {
        return new ItemDetails().tap {
            brandName = BR_NAME_1
            brandId = 1L
            itemId = 1L
            itemsCount = 10L
        }
    }
}
