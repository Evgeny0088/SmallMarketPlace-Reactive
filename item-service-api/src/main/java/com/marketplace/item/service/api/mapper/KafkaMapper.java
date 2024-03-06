package com.marketplace.item.service.api.mapper;

import com.marketplace.item.service.api.dto.ItemTransactionResponse;
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
