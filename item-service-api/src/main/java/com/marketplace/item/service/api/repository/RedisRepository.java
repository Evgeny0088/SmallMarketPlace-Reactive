package com.marketplace.item.service.api.repository;

import com.marketplace.item.service.api.dto.ItemUpdatePipeline;
import reactor.core.publisher.Mono;

public interface RedisRepository {

    Mono<ItemUpdatePipeline> invalidateCacheOnItemUpdate(ItemUpdatePipeline pipeline);

    Mono<Void> cleanupCacheOnDeletedBrand(long brandId);

    Mono<ItemUpdatePipeline> cleanupCacheOnDeletedItem(ItemUpdatePipeline pipeline);

}
