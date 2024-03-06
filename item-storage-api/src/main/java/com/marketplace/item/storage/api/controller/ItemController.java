package com.marketplace.item.storage.api.controller;

import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.dto.*;
import com.marketplace.item.storage.api.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.util.retry.Retry;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = Constants.API_PREFIX_ITEMS)
@RequiredArgsConstructor
@Validated
public class ItemController implements Constants {
	
	private final ItemService itemService;
	private final KafkaSender<String, Object> kafkaSender;

	@PostMapping
	public ResponseEntity<Mono<ItemResponse>> createItem(@Valid @RequestBody ItemRequest request) {
		return ResponseEntity.ok(itemService.createItem(request));
	}

	@PostMapping(DELETE)
	public ResponseEntity<Mono<ItemResponse>> deleteItem(@RequestHeader(HEADER_PROFILE_ID) String profileId, @Valid @RequestBody ItemUpdateRequest request) {
		return ResponseEntity.ok(itemService.deleteItem(request, profileId));
	}

	@PostMapping(REQUEST_FOR_SELL)
	public Mono<ItemTransactionResponse> requestForSell(@Valid @RequestBody ItemTransactionRequest request) {
		return itemService.sellItems(request);
	}

	/*
	########### end points only for testing ################
	*/

	/*
	try to sell items through kafka topic request
	*/
	@PostMapping("/kafka-transaction-test")
	public ResponseEntity<Mono<Void>> transactionTest(@RequestBody ItemTransactionRequest request) {
		String topic = "item.transaction.request";
		ItemDetails details = ItemDetails.builder()
				.brandName("nike")
				.itemId(1L)
				.itemsCount(1L)
				.brandId(1L)
				.build();

		request.setItemDetails(details);

		kafkaSender.createOutbound()
				.send(Mono.just(new ProducerRecord<>(topic, request.getTransactionId().toString(), request)))
				.then()
				.retryWhen(Retry.max(3))
				.doOnSuccess(item -> log.info(String.format("Object { %s } is sent to topic %s", request, topic)))
				.doOnError(e -> log.error(String.format("Failed to send object: %s to topic: %s", request, topic)))
				.subscribe();
		return ResponseEntity.ok(Mono.empty());
	}
}
