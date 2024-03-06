package com.marketplace.item.service.api.service;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.common.MessageConstants;
import com.marketplace.item.service.api.dto.*;
import com.marketplace.item.service.api.entity.Item;
import com.marketplace.item.service.api.enums.ItemType;
import com.marketplace.item.service.api.enums.TransactionStatus;
import com.marketplace.item.service.api.mapper.ItemServiceMapper;
import com.marketplace.item.service.api.repository.ItemRepository;
import com.marketplace.item.service.api.repository.SellTransactionRepository;
import com.marketplace.item.service.api.mapper.KafkaMapper;
import com.marketplace.item.service.api.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import static com.marketplace.item.service.api.utils.ItemServiceUtils.verifyItemType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService implements Constants, MessageConstants {

	private final ItemRepository itemRepository;
	private final SellTransactionRepository sellTransactionRepository;
	private final ItemServiceMapper itemMapper;
	private final RedisRepository redisRepository;
	private final KafkaMapper kafkaMapper;

	public Mono<ItemResponse> createItem(ItemRequest request) {
		ItemUpdatePipeline pipeline = ItemUpdatePipeline.builder()
				.itemCreateRequest(request)
				.build();
		return Mono.fromCallable(()-> verifyItemType(pipeline))
				.flatMap(itemRepository::saveNewItem)
				.flatMap(redisRepository::invalidateCacheOnItemUpdate)
				.map(p-> itemMapper.fromItemOnCreate(p.getItem(), OBJECT_CREATED))
				.onErrorMap(Exceptions::propagate)
				.log();
	}

	public Mono<ItemResponse> deleteItem(ItemUpdateRequest request, String profileId) {
		ItemUpdatePipeline pipeline = ItemUpdatePipeline.builder().itemUpdateRequest(request).build();
		return itemRepository.getFullItemById(pipeline)
				.flatMap(p-> {
					Item item = p.getItem();
					if (!item.getBrand().getOwnerId().equals(profileId)) {
						return Mono.error(new CustomException(HttpStatus.FORBIDDEN).setDetails(String.format(DELETE_ONLY_BY_OWNER, profileId)));
					}
					Mono<ItemResponse> answer = Mono.empty();
					if (item.getItemType().equals(ItemType.ITEM)) {
						setupItemDetails(pipeline, item);
						answer =
								itemRepository.deleteItemById(request)
										.then(itemRepository.getItemDetailsWithBrandName(pipeline))
										.flatMap(id-> redisRepository.invalidateCacheOnItemUpdate(pipeline))
										.thenReturn(itemMapper.onItemDelete(request.getId()));
					}
					if (item.getItemType().equals(ItemType.PACK)) {
						answer =
								itemRepository.deleteItemById(request)
										.then(redisRepository.cleanupCacheOnDeletedItem(pipeline))
										.thenReturn(itemMapper.onItemDelete(request.getId()));
					}
					return answer;
				});
	}

	@Transactional
	public Mono<ItemTransactionResponse> sellItems(ItemTransactionRequest transactionRequest) {
		return sellTransactionRepository.tryToSaveTransaction(transactionRequest)
				.flatMap(itemRepository::getItemDetails)
				.filter(pipeline -> {
					ItemTransactionRequest request = pipeline.getTransactionRequest();
					ItemDetails details = pipeline.getItemDetails();
					if (details.getItemsCount() < request.getItemDetails().getItemsCount()) {
						throw new CustomException(HttpStatus.BAD_REQUEST)
										.setDetails(String.format(SELLING_ERROR_COUNT, details.getItemsCount(), request.getItemDetails().getItemsCount()));
					}
					return true;
				})
				.flatMap(itemRepository::removeSoldItems)
				.flatMap(redisRepository::invalidateCacheOnItemUpdate)
				.map(pipeline -> 
						kafkaMapper.toTransactionResponse(
								pipeline.getTransactionRequest().getTransactionId().toString(), 
								String.format(
										SOLD_SUCCESSFULLY, 
										pipeline.getTransactionRequest().getItemDetails().getItemsCount(), 
										pipeline.getItemDetails().getItemId()
								),
								TransactionStatus.OK.name())
				);
	}

	private void setupItemDetails(ItemUpdatePipeline pipeline, Item item) {
		pipeline.setTransactionRequest(ItemTransactionRequest.builder().build());
		ItemDetails details = ItemDetails.builder()
				.itemId(item.getParentItem().getId())
				.brandId(item.getBrand().getId())
				.build();
		pipeline.getTransactionRequest().setItemDetails(details);
	}
}
