package com.marketplace.itemstorage.dto;

import com.marketplace.itemstorage.enums.ItemType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ItemRequest {
	
	@NotNull
	private Long brandId;
	private Long parentItem;
	@NotNull
	private ItemType itemType;
}
