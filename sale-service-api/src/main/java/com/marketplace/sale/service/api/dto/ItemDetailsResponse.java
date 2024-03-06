package com.marketplace.sale.service.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDetailsResponse {

    private Long nextEvaluatedKey;
    private Long prevEvaluatedKey;
    private ItemsContent content;

}
