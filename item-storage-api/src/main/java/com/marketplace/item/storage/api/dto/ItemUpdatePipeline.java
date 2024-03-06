package com.marketplace.item.storage.api.dto;

import com.marketplace.item.storage.api.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdatePipeline {

    private ItemTransactionRequest transactionRequest;
    private ItemUpdateRequest itemUpdateRequest;
    private ItemRequest itemCreateRequest;
    private ItemDetails itemDetails;
    private Item item;

}
