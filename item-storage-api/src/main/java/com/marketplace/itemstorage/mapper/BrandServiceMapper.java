package com.marketplace.itemstorage.mapper;

import com.marketplace.itemstorage.dto.BrandResponse;
import com.marketplace.itemstorage.entity.Brand;
import org.springframework.stereotype.Component;

import static com.marketplace.itemstorage.common.Constants.BRAND_NAME;
import static com.marketplace.itemstorage.common.MessageConstants.OBJECT_IS_DELETED;

@Component
public class BrandServiceMapper {
	
	public BrandResponse fromBrandOnCreate(Brand brand, String message) {
		return BrandResponse.builder()
			       .message(message)
			       .id(brand.getId())
			       .brandName(brand.getBrandName())
			       .createdAt(brand.getCreatedAt())
			       .build();
	}
	
	public BrandResponse fromBrandOnUpdate(Brand brand, String message) {
		return BrandResponse.builder()
			       .id(brand.getId())
			       .brandName(brand.getBrandName())
			       .updateAt(brand.getUpdatedAt())
			       .message(message)
			       .build();
	}
	
	public BrandResponse withMessageOnly(String message) {
		return BrandResponse.builder()
			.message(String.format(OBJECT_IS_DELETED, BRAND_NAME, message))
			.build();
	}
}
