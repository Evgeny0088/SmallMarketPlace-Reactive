package com.marketplace.sale.service.api.controller;

import com.marketplace.sale.service.api.dto.ItemDetailsResponse;
import com.marketplace.sale.service.api.dto.PageableRequest;
import com.marketplace.sale.service.api.dto.SaleResponse;
import com.marketplace.sale.service.api.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.marketplace.sale.service.api.common.Constants.*;

@RestController
@RequestMapping(path = SALE_ROOT_PATH)
@RequiredArgsConstructor
public class SalesController {

    private final SaleService saleService;

    @PostMapping(GET_ITEMS_PAGE)
    public ResponseEntity<Mono<ItemDetailsResponse>> getItemsInfo(@Valid @RequestBody PageableRequest request) {
        return ResponseEntity.ok(saleService.getPageableItems(request));
    }

    @PostMapping(SELL_ITEM)
    public ResponseEntity<Mono<SaleResponse>> sellItems(@PathVariable("brand-id") long brandId, @PathVariable("id") long itemId, Long count) {
        return ResponseEntity.ok(saleService.sellItem(brandId, itemId, count));
    }
}