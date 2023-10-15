package com.marketplace.itemstorage.dto;

import com.marketplace.itemstorage.enums.ItemType;
import com.marketplace.itemstorage.validation.FiledRequirementValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateRequest {

	@FiledRequirementValidation
	private Long id;
	private Long brandId;
	private ItemType itemType;
}
