package com.marketplace.sale.service.api.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemDetails {

    @EqualsAndHashCode.Include
    private Long itemId;

    private Long brandId;
    private Long itemsCount;
    private String brandName;

}
