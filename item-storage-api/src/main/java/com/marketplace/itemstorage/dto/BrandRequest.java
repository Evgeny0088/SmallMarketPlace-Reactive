package com.marketplace.itemstorage.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class BrandRequest {
	
	@NotBlank
	private String brandName;
	private String updatedName;
	private Long id;
}
