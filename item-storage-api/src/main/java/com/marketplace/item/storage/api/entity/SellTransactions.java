package com.marketplace.item.storage.api.entity;

import com.marketplace.item.storage.api.common.Constants;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Column;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static com.marketplace.item.storage.api.common.Constants.TRANSACTIONS_TABLE_NAME;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(TRANSACTIONS_TABLE_NAME)
public class SellTransactions implements Constants {

    @Id
    @Column(name = ID, nullable = false)
    private Long transactionId;

    @Column(name = Constants.CREATED_AT, nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
