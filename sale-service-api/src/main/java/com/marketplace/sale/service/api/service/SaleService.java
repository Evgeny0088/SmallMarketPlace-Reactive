package com.marketplace.sale.service.api.service;

import com.marketplace.sale.service.api.dto.*;
import com.marketplace.sale.service.api.enums.PageState;
import com.marketplace.sale.service.api.mapper.SaleServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService {

    private final RemoteCallsService remoteCallsService;
    private final SaleServiceMapper serviceMapper;
    private final Map<PageState, Function<PageableRequest, Flux<ItemDetails>>> redisPageResponseMap;

    public Mono<ItemDetailsResponse> getPageableItems(PageableRequest request) {
        return prepareRequest(request)
                .flatMapMany(r-> redisPageResponseMap.get(r.getPageState()).apply(r))
                .collectList()
                .map(details -> serviceMapper.buildItemResponse(details, request))
                .onErrorMap(Exceptions::propagate);
    }

    public Mono<SaleResponse> sellItem(long brandId, long itemId, Long count) {
        return prepareTransactionRequest(brandId, itemId, count)
                .flatMap(remoteCallsService::requestForSale)
                .map(serviceMapper::toSaleResponse)
                .onErrorMap(Exceptions::propagate);
    }

    public Mono<PageableRequest> prepareRequest(PageableRequest request) {
        return Mono.fromSupplier(() -> {
            Long size = request.getSize();
            request.setSize(size != null && size > 0 ? size : 5L);
            if (ObjectUtils.isNotEmpty(request.getNextEvaluatedKey())) {
                request.setPageState(PageState.NEXT_KEY);
            } else if (ObjectUtils.isNotEmpty(request.getPrevEvaluatedKey())) {
                request.setPageState(PageState.PREV_KEY);
            }
            return request;
        });
    }

    public Mono<ItemTransactionRequest> prepareTransactionRequest(long brandId, long itemId, Long count) {
        return Mono.fromSupplier(()-> {
            ItemDetails details = ItemDetails.builder()
                    .brandId(brandId)
                    .itemId(itemId)
                    .itemsCount(count == null || count == 0 ? 1 : count)
                    .build();
           return ItemTransactionRequest.builder()
                    .transactionId(System.currentTimeMillis())
                    .itemDetails(details)
                    .build();
        });
    }
}
