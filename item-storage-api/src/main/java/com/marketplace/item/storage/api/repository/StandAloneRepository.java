package com.marketplace.item.storage.api.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.item.storage.api.dto.ItemDetails;
import com.marketplace.item.storage.api.dto.ItemUpdatePipeline;
import com.marketplace.kafka.starter.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.connection.ReactiveZSetCommands;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.marketplace.item.storage.api.common.Constants.ITEMS_COLLECTION_SET;
import static com.marketplace.item.storage.api.utils.ItemServiceUtils.createKeyFromString;

@Slf4j
@Repository
@Getter
@RequiredArgsConstructor
public class StandAloneRepository implements RedisRepository, Constants {

    private final ObjectMapper mapper;
    private final ReactiveZSetCommands sortedCommands;
    private final ReactiveKeyCommands keyCommands;

    @Override
    public Mono<ItemUpdatePipeline> invalidateCacheOnItemUpdate(ItemUpdatePipeline pipeline) {
        ItemDetails details = pipeline.getItemDetails();
        if (details == null) return Mono.just(pipeline);

        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(details.getBrandId()));
        double score = Double.valueOf(details.getItemId());
        return
                details.getItemsCount() > 0
                        ? removeByScore(byteKey, score)
                            .then(appendToCollection(byteKey, score, details))
                            .thenReturn(pipeline)
                        : removeByScore(byteKey, score).thenReturn(pipeline);
    }

    @SneakyThrows
    @Override
    public Mono<Void> cleanupCacheOnDeletedBrand(long brandId) {
        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(brandId));
        return keyCommands.del(byteKey).then();
    }

    @SneakyThrows
    @Override
    public Mono<ItemUpdatePipeline> cleanupCacheOnDeletedItem(ItemUpdatePipeline pipeline) {
        long key = pipeline.getItem().getBrand().getId();
        double score = (double) pipeline.getItem().getId();
        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(key));
        return removeByScore(byteKey, score).thenReturn(pipeline);
    }

    @SneakyThrows
    public Mono<Long> appendToCollection(ByteBuffer byteKey, double score, Object details) {
        return sortedCommands.zAdd(
                byteKey,
                score,
                ByteBuffer.wrap(mapper.writeValueAsBytes(details)));
    }

    private Mono<Void> removeByScore(ByteBuffer byteKey, double score) {
        return sortedCommands.zRemRangeByScore(
                        byteKey,
                        Range.of(Range.Bound.inclusive(Double.valueOf(score)), Range.Bound.inclusive(Double.valueOf(score)))
                )
                .then();
    }

    /*
    methods used for test only
    */
    public Mono<Boolean> ifAnyExists() {
        return keyCommands.scan().collectList().map(list-> !list.isEmpty());
    }

    public Mono<ItemDetails> findItemDetails(long brandId) {
        ByteBuffer byteKey = createKeyFromString(ITEMS_COLLECTION_SET, String.valueOf(brandId));
        return sortedCommands.zRandMember(byteKey)
                .switchIfEmpty(Mono.empty())
                .handle((bytes, sink) -> {
                    try {
                        sink.next(mapper.readValue(StandardCharsets.UTF_8.decode(bytes).toString(), ItemDetails.class));
                    }
                    catch (JsonProcessingException e) {
                        sink.error(new RuntimeException(e));
                    }});
    }

    public Mono<Void> flushAll() {
        return keyCommands.scan()
                .log()
                .flatMap(keyCommands::del)
                .then();
    }
}
