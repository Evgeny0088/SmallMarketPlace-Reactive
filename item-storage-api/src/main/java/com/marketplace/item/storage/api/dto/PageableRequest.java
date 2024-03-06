package com.marketplace.item.storage.api.dto;

import com.marketplace.item.storage.api.enums.PageState;
import lombok.Data;

@Data
public class PageableRequest {
    
    private String order;
    private Long prevEvaluatedKey;
    private Long nextEvaluatedKey;
    private Integer size;
    private PageState pageState;

}
