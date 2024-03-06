package com.marketplace.item.storage.api.repository;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.common.MessageConstants;
import com.marketplace.item.storage.api.dto.ItemTransactionRequest;
import com.marketplace.item.storage.api.dto.ItemUpdatePipeline;
import com.marketplace.item.storage.api.entity.SellTransactions;
import com.marketplace.item.storage.api.mapper.ItemServiceMapper;
import com.marketplace.item.storage.api.mapper.SellRepositoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SellTransactionRepository implements Constants, MessageConstants {

    private final DatabaseClient client;
    private final SellRepositoryMapper sellRepositoryMapper;
    private final ItemServiceMapper itemServiceMapper;

    public Mono<ItemUpdatePipeline> tryToSaveTransaction(ItemTransactionRequest request) {
        return client.sql(
                        """
                        select * from sell_transactions as t where t.id=:id 
                        """
                )
                .bind(ID, request.getTransactionId())
                .map(sellRepositoryMapper::mapToSellTransaction)
                .one()
                .<SellTransactions>handle((tr, sink)-> {
                    if (Objects.equals(tr.getTransactionId(), request.getTransactionId())) {
                        CustomException ex = new CustomException(HttpStatus.BAD_REQUEST)
                                .setDetails(String.format(TRANSACTION_ALREADY_PROCESSED, request.getTransactionId()));
                        sink.error(ex);
                    }
                })
                .then(saveNewTransaction(request))
                .map(itemServiceMapper::addTransactionRequest);
    }

    public Mono<ItemTransactionRequest> saveNewTransaction(ItemTransactionRequest dto) {
        return client.sql(
                        """
                        insert into sell_transactions(id, created_at) values (:id, current_timestamp) returning sell_transactions.id;
                        """
                )
                .bind(ID, dto.getTransactionId())
                .then()
                .thenReturn(dto);
    }

    /*
    test methods
    */
    public Mono<Void> deleteAll() {
        return client.sql(
                """
                delete from sell_transactions
                """
        ).then();
    }
}
