package com.marketplace.sale.service.api.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.sale.service.api.common.Constants;
import com.marketplace.sale.service.api.dto.ItemDetails;
import com.marketplace.sale.service.api.dto.PageableRequest;
import com.marketplace.sale.service.api.enums.PageState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.ReactiveZSetCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.marketplace.sale.service.api.utils.SaleOrdersUtils.createKeyFromString;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StandAloneRepository implements RedisRepository, Constants {

    private final ObjectMapper mapper;
    private final ReactiveZSetCommands sortedCommands;

    @SneakyThrows
    @Override
    public Flux<ItemDetails> getFirstPage(PageableRequest request) {
        return sortedCommands
                .zRangeByScore(
                        createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(request.getId())),
                        setupRange(request.getPageState(), 0),
                        setupLimit(request.getSize().intValue())
                )
                .flatMap(this::itemTransform);
    }

    @Override
    public Flux<ItemDetails> getNextPage(PageableRequest request) {
        return sortedCommands
                .zRangeByScore(
                        createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(request.getId())),
                        setupRange(request.getPageState(), request.getNextEvaluatedKey()),
                        setupLimit(request.getSize().intValue())
                )
                .flatMap(this::itemTransform);
    }

    @Override
    public Flux<ItemDetails> getPrevPage(PageableRequest request) {
        return sortedCommands
                .zRevRangeByScore(
                        createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(request.getId())),
                        setupRange(request.getPageState(), request.getPrevEvaluatedKey()),
                        setupLimit(request.getSize().intValue())
                )
                .flatMap(this::itemTransform);
    }

    private Mono<ItemDetails> itemTransform(ByteBuffer bytes) {
        try {
            return Mono.just(mapper.readValue(StandardCharsets.UTF_8.decode(bytes).toString(), ItemDetails.class));
        }
        catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException(e));
        }
    }

    private RedisZSetCommands.Limit setupLimit(int count) {
        return RedisZSetCommands.Limit.limit().offset(0).count(count);
    }

    private Range<Double> setupRange(PageState direction, long lastKey) {
        return direction == PageState.FIRST_PAGE || direction == PageState.NEXT_KEY
                ? Range.rightUnbounded(Range.Bound.exclusive(Double.valueOf(String.valueOf(lastKey))))
                : Range.leftUnbounded(Range.Bound.exclusive(Double.valueOf(String.valueOf(lastKey))));
    }
}
