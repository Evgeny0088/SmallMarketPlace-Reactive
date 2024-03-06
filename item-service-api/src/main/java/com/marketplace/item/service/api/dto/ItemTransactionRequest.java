package com.marketplace.item.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransactionRequest {

    private Long transactionId;
    private ItemDetails itemDetails;

}
