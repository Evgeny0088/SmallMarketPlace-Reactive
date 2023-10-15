package com.marketplace.itemstorage.mapper;

import com.marketplace.itemstorage.dto.ItemResponse;
import com.marketplace.itemstorage.entity.Item;
import org.springframework.stereotype.Component;

import static com.marketplace.itemstorage.common.Constants.ITEM_ID;
import static com.marketplace.itemstorage.common.MessageConstants.OBJECT_IS_DELETED;

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
				.message(String.format(OBJECT_IS_DELETED, ITEM_ID, id))
				.build();
	}
}
