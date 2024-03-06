package com.marketplace.item.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetails {

    private String brandName;
    private Long brandId;
    private Long itemId;
    private Long itemsCount;

}
