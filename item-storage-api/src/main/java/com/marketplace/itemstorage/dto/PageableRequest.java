package com.marketplace.itemstorage.dto;

import com.marketplace.itemstorage.enums.PageState;
import lombok.Data;

@Data
public class PageableRequest {
    
    private String order;
    private Long prevEvaluatedKey;
    private Long nextEvaluatedKey;
    private Integer size;
    private PageState pageState;

}
