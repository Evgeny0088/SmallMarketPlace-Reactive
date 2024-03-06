package com.marketplace.sale.service.api.mapper;

import com.marketplace.sale.service.api.dto.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class SaleServiceMapper {

    public List<ItemDetails> defineSorting(List<ItemDetails> details, PageableRequest request) {
        if (ObjectUtils.isNotEmpty(request.getPrevEvaluatedKey())) {
            return details.stream().sorted(Comparator.comparing(ItemDetails::getItemId)).toList();
        }
        return details;
    }

    public ItemsContent toItemContent(List<ItemDetails> items, int pageSize) {
        return
                ItemsContent.builder()
                        .pageSize(pageSize)
                        .countOfElements(items.size())
                        .items(items.isEmpty() ? new ArrayList<>() : items)
                        .build();
    }

    public Long setupNextKey(List<ItemDetails> items, PageableRequest request) {
        int size = items.size();
        return size >= request.getSize()
                ? items.get(size - 1).getItemId()
                : null;
    }

    public Long setupPrevKey(List<ItemDetails> items, PageableRequest request) {
        int size = items.size();
        Long requestPrevKey = request.getPrevEvaluatedKey();
        Long requestNextKey = request.getNextEvaluatedKey();

        if (ObjectUtils.isEmpty(requestPrevKey) && ObjectUtils.isEmpty(requestNextKey)) {
            return null;
        }
        else if (size == 0 && ObjectUtils.isNotEmpty(requestNextKey)) {
            return requestNextKey;
        }
        else if (size > 0) {
            return items.get(0).getItemId();
        }
        return null;
    }

    public ItemDetailsResponse buildItemResponse(List<ItemDetails> details, PageableRequest request) {
        details = defineSorting(details, request);
        return ItemDetailsResponse.builder()
                .content(toItemContent(details, request.getSize().intValue()))
                .nextEvaluatedKey(setupNextKey(details, request))
                .prevEvaluatedKey(setupPrevKey(details, request))
                .build();
    }

    public SaleResponse toSaleResponse(ItemTransactionResponse response) {
        return SaleResponse.builder()
                .transactionId(response.getTransactionId())
                .transactionStatus(response.getTransactionStatus())
                .message(response.getMessage())
                .build();
    }
}
