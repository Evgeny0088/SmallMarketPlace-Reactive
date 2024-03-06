package com.marketplace.sale.service.api.config;

import com.marketplace.sale.service.api.dto.ItemDetails;
import com.marketplace.sale.service.api.dto.PageableRequest;
import com.marketplace.sale.service.api.enums.PageState;
import com.marketplace.sale.service.api.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class SaleServiceConfig {

    private final RedisRepository redisRepository;

    @Bean
    public Map<PageState, Function<PageableRequest, Flux<ItemDetails>>> itemPageCallableMap() {
        Map<PageState, Function<PageableRequest, Flux<ItemDetails>>> publishMap = new HashMap<>();
        publishMap.put(PageState.FIRST_PAGE, redisRepository::getFirstPage);
        publishMap.put(PageState.PREV_KEY, redisRepository::getPrevPage);
        publishMap.put(PageState.NEXT_KEY, redisRepository::getNextPage);
        return publishMap;
    }
}
