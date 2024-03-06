package com.marketplace.item.storage.api.dto;

import com.marketplace.item.storage.api.enums.ItemType;
import com.marketplace.item.storage.api.validation.FieldRequirementValidation;
import lombok.Data;

@Data
public class ItemUpdateRequest {

	@FieldRequirementValidation
	private Long id;
	private Long brandId;
	private ItemType itemType;
}
