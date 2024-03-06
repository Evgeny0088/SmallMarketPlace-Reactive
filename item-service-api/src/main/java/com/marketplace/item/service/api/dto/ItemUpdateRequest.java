package com.marketplace.item.service.api.dto;

import com.marketplace.item.service.api.enums.ItemType;
import com.marketplace.item.service.api.validation.FieldRequirementValidation;
import lombok.Data;

@Data
public class ItemUpdateRequest {

	@FieldRequirementValidation
	private Long id;
	private Long brandId;
	private ItemType itemType;
}
