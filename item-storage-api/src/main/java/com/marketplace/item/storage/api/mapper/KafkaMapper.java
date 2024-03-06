package com.marketplace.item.storage.api.mapper;

import com.marketplace.item.storage.api.dto.ItemTransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class KafkaMapper {

    public ItemTransactionResponse toTransactionResponse(String transactionId, String message, String transactionStatus) {
        return ItemTransactionResponse.builder()
                .transactionId(transactionId)
                .message(message)
                .transactionStatus(transactionStatus)
                .build();
    }
}
