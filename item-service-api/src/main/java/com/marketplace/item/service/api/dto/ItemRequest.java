package com.marketplace.item.service.api.dto;

import com.marketplace.item.service.api.enums.ItemType;
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
