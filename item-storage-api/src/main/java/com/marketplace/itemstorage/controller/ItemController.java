package com.marketplace.itemstorage.controller;

import com.marketplace.itemstorage.dto.ItemRequest;
import com.marketplace.itemstorage.dto.ItemResponse;
import com.marketplace.itemstorage.dto.ItemUpdateRequest;
import com.marketplace.itemstorage.service.ItemService;
import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.aspect.annotations.RolesRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.marketplace.itemstorage.common.Constants.API_PREFIX_ITEMS;

@RestController
@RequestMapping(path = API_PREFIX_ITEMS)
@RequiredArgsConstructor
@Validated
public class ItemController {
	
	private final ItemService itemService;

//	@Owner
	@PostMapping
	public ResponseEntity<Mono<ItemResponse>> createItem(@Valid @RequestBody ItemRequest request) {
		return ResponseEntity.ok(itemService.createItem(request));
	}

//	@Owner
	@PutMapping
	public ResponseEntity<Mono<ItemResponse>> updateItem(@Valid @RequestBody ItemUpdateRequest request) {
		return ResponseEntity.ok(itemService.updateItem(request));
	}

//	@Owner
//	@RolesRequired(roles = {"ADMIN"})
	@DeleteMapping
	public ResponseEntity<Mono<ItemResponse>> deleteItem(@Valid @RequestBody ItemUpdateRequest request) {
		return ResponseEntity.ok(itemService.deleteItem(request));
	}
}
