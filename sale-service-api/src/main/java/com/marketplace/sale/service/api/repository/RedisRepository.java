package com.marketplace.sale.service.api.repository;

import com.marketplace.sale.service.api.dto.ItemDetails;
import com.marketplace.sale.service.api.dto.PageableRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedisRepository {

    Flux<ItemDetails> getFirstPage(PageableRequest request);

    Flux<ItemDetails> getNextPage(PageableRequest request);

    Flux<ItemDetails> getPrevPage(PageableRequest request);

}
