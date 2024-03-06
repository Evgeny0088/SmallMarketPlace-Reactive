package com.marketplace.sale.service.api.data

import com.marketplace.sale.service.api.dto.ItemDetails
import com.marketplace.sale.service.api.dto.PageableRequest
import com.marketplace.sale.service.api.enums.PageState
import com.marketplace.sale.service.api.common.TestConstants

class TestData implements TestConstants {

    static TestSellRequest sellRequest_1() {
        return new TestSellRequest().tap {
            brandId = BRAND_ID
            itemId = 1
            itemCount = 1
        }
    }

    static PageableRequest pageableRequest_1() {
        return new PageableRequest().tap {
            pageState = PageState.FIRST_PAGE
            id = BRAND_ID
        }
    }

    static PageableRequest pageableRequest_0() {
        return new PageableRequest().tap {
            pageState = PageState.FIRST_PAGE
        }
    }

    static List<ItemDetails> savedItems_0() {
        return []
    }

    static List<ItemDetails> savedItems_1() {
        List<ItemDetails> list = new ArrayList<>()
        for (int i in 1..5) {
            list.add(savedInRedis_1(i, i))
        }
        return list
    }

    static ItemDetails savedInRedis_1(long id, long count) {
        return new ItemDetails().tap {
            brandId = BRAND_ID
            itemId = id
            itemsCount = count
            brandName = BR_NAME_1
        }
    }

}
