package com.marketplace.item.storage.api.repository;

import com.marketplace.item.storage.api.dto.ItemUpdatePipeline;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

public interface RedisRepository {

    Mono<ItemUpdatePipeline> invalidateCacheOnItemUpdate(ItemUpdatePipeline pipeline);

    Mono<Void> cleanupCacheOnDeletedBrand(long brandId);

    Mono<ItemUpdatePipeline> cleanupCacheOnDeletedItem(ItemUpdatePipeline pipeline);

}
