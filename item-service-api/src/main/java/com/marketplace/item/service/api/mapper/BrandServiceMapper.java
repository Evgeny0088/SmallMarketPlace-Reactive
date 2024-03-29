package com.marketplace.item.service.api.mapper;

import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.common.MessageConstants;
import com.marketplace.item.service.api.entity.Brand;
import com.marketplace.item.service.api.dto.BrandResponse;
import org.springframework.stereotype.Component;

@Component
public class BrandServiceMapper {
	
	public BrandResponse fromBrandOnCreate(Brand brand, String message) {
		return BrandResponse.builder()
			       .message(message)
			       .id(brand.getId())
			       .brandName(brand.getBrandName())
					.ownerId(brand.getOwnerId())
			       .createdAt(brand.getCreatedAt())
			       .build();
	}
	
	public BrandResponse fromBrandOnUpdate(Brand brand, String message) {
		return BrandResponse.builder()
			       .id(brand.getId())
			       .brandName(brand.getBrandName())
					.ownerId(brand.getOwnerId())
			       .updatedAt(brand.getUpdatedAt())
			       .message(message)
			       .build();
	}
	
	public BrandResponse withMessageOnly(String message) {
		return BrandResponse.builder()
			.message(String.format(MessageConstants.OBJECT_IS_DELETED, Constants.BRAND_NAME, message))
			.build();
	}
}
