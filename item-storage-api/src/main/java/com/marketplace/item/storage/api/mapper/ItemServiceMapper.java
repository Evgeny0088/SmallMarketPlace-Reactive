package com.marketplace.item.storage.api.mapper;

import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.common.MessageConstants;
import com.marketplace.item.storage.api.dto.*;
import com.marketplace.item.storage.api.entity.Brand;
import com.marketplace.item.storage.api.entity.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemServiceMapper {
	
	public ItemResponse fromItemOnCreate(Item item, String message) {
		Item parent = item.getParentItem();
		Long parentId = parent == null ? null : parent.getId();
		long brandId = item.getBrand().getId();
		return ItemResponse.builder()
			       .message(message)
			       .id(item.getId())
			       .parentId(parentId)
			       .itemType(item.getItemType().name())
			       .createdAt(item.getCreatedAt())
			       .brandId(brandId)
			       .build();
	}

	public ItemResponse onItemDelete(Long id) {
		return ItemResponse.builder()
				.id(id)
				.message(String.format(MessageConstants.OBJECT_IS_DELETED, Constants.ITEM_ID, id))
				.build();
	}

	public ItemUpdatePipeline addTransactionRequest(ItemTransactionRequest request) {
		return ItemUpdatePipeline.builder()
				.transactionRequest(request)
				.build();
	}

	public ItemUpdatePipeline addItemDetails(ItemUpdatePipeline pipeline, ItemDetails details) {
		details.setBrandName(pipeline.getTransactionRequest().getItemDetails().getBrandName());
		pipeline.setItemDetails(details);
		return pipeline;
	}

	public ItemUpdatePipeline updateItemDetailsCount(ItemUpdatePipeline pipeline) {
		long countBeforeSell = pipeline.getItemDetails().getItemsCount();
		long countToSell = pipeline.getTransactionRequest().getItemDetails().getItemsCount();
		pipeline.getItemDetails().setItemsCount(countBeforeSell - countToSell);
		return pipeline;
	}

	public ItemDetails fromItem(Item item) {
		Item parent = item.getParentItem();
		long itemId = parent != null && parent.getId() != null ? parent.getId() : item.getId();
		return ItemDetails.builder()
				.brandId(item.getBrand().getId())
				.brandName(item.getBrand().getBrandName())
				.itemId(itemId)
				.itemsCount((long) item.getChildItems().size())
				.build();
	}

	public Item deletedItem(long id, long brandId) {
		return Item.builder()
				.id(id)
				.brand(Brand.builder().id(brandId).build())
				.build();
	}
}
