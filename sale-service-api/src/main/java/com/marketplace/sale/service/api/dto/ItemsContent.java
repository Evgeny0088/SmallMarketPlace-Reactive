package com.marketplace.sale.service.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemsContent {

    private int pageSize;
    private int countOfElements;
    private List<ItemDetails> items;

}
