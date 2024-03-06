package com.marketplace.item.service.api.mapper;

import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.entity.SellTransactions;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.marketplace.item.service.api.utils.ItemServiceUtils.parseNullableLong;
import static com.marketplace.item.service.api.utils.ItemServiceUtils.parseNullableString;

@Component
public class SellRepositoryMapper implements Constants {

    public SellTransactions mapToSellTransaction(Row row) {
        String createdAt = parseNullableString(row.get(CREATED_AT));
        return SellTransactions.builder()
                .transactionId(parseNullableLong(row.get(ID)))
                .createdAt(LocalDateTime.parse(createdAt == null ? LocalDateTime.now().toString() : createdAt))
                .build();
    }

}
