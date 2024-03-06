package com.marketplace.item.storage.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
	
	@NotBlank
	private String brandName;
	private String updatedName;
	private Long id;
}
