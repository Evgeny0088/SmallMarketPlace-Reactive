package com.marketplace.itemstorage.service;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.common.MessageConstants;
import com.marketplace.itemstorage.dto.ItemRequest;
import com.marketplace.itemstorage.dto.ItemResponse;
import com.marketplace.itemstorage.dto.ItemUpdateRequest;
import com.marketplace.itemstorage.mapper.ItemServiceMapper;
import com.marketplace.itemstorage.properties.ItemProperties;
import com.marketplace.itemstorage.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.marketplace.itemstorage.utils.ItemServiceUtils.verifyItemType;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService implements Constants, MessageConstants {

	private final ItemRepository itemRepository;
	private final ItemServiceMapper itemMapper;
	private final ItemProperties itemProperties;

	public Mono<ItemResponse> createItem(ItemRequest request) {
		return Mono.fromCallable(
						()-> verifyItemType(request))
				.flatMap(itemRepository::saveNewItem)
				.map(item-> itemMapper.fromItemOnCreate(item, OBJECT_CREATED))
				.onErrorMap(Exceptions::propagate)
				.log();
	}

	public Mono<ItemResponse> updateItem(ItemUpdateRequest request) {
		return itemRepository.updateItem(request)
				.map(item-> itemMapper.fromItemOnCreate(item, String.format(OBJECT_UPDATED, request.getId())));
	}

	public Mono<ItemResponse> deleteItem(ItemUpdateRequest request) {
		return itemRepository.deleteItemById(request)
				.map(itemMapper::onItemDelete)
				.onErrorMap(Exceptions::propagate)
				.log();
	}
}
