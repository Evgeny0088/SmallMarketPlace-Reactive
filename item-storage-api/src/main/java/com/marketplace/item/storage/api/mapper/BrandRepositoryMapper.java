package com.marketplace.item.storage.api.mapper;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.common.MessageConstants;
import com.marketplace.item.storage.api.dto.BrandRequest;
import com.marketplace.item.storage.api.entity.Brand;
import com.marketplace.item.storage.api.entity.Item;
import com.marketplace.item.storage.api.utils.ItemServiceUtils;
import io.r2dbc.spi.Row;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.marketplace.item.storage.api.utils.ItemServiceUtils.parseNullableLong;
import static com.marketplace.item.storage.api.utils.ItemServiceUtils.parseNullableType;

@Component
public class BrandRepositoryMapper implements Constants, MessageConstants {
	
	public Brand fromCreate(Row row, BrandRequest request, String ownerId) {
		return Brand.builder()
			       .id(parseNullableLong(row.get(ID)))
					.ownerId(ownerId)
			       .brandName(request.getBrandName())
			       .createdAt(LocalDateTime.now())
			       .build();
	}
	
	public Brand onUpdateName(Brand brand, String ownerId, String updatedName) {
		if (!brand.getOwnerId().equals(ownerId)) {
			throw new CustomException(HttpStatus.FORBIDDEN).setDetails(String.format(UPDATE_ONLY_BY_OWNER, ownerId));
		}
		brand.setBrandName(updatedName);
		brand.setUpdatedAt(LocalDateTime.now());
		return brand;
	}
	
	public Brand fromRowResult(Row row) {
		return Brand.builder()
				.id(row.get(ID, Long.class))
				.ownerId(row.get(OWNER_ID, String.class))
				.brandName(row.get(BRAND_NAME, String.class))
				.createdAt(row.get(CREATED_AT, LocalDateTime.class))
				.updatedAt(row.get(UPDATED_AT, LocalDateTime.class))
				.build();
	}

	public Brand brandFromResultMap(Map<String, Object> resultMap, String idName) {
		return Brand.builder()
			       .id(Long.parseLong(resultMap.get(idName).toString()))
			       .brandName(resultMap.get(BRAND_NAME).toString())
				   .ownerId(resultMap.get(OWNER_ID).toString())
			       .createdAt(parseNullableTime(resultMap.get(BRAND_CREATED_AT)))
			       .updatedAt(parseNullableTime(resultMap.get(BRAND_UPDATED_AT)))
			       .build();
	}
	
	public Item itemFromResultMap(Map<String, Object> resultMap) {
		return Item.builder()
			       .id(parseNullableLong(resultMap.get(ITEM_ID)))
			       .createdAt(parseNullableTime(resultMap.get(ITEM_CREATED_AT)))
			       .itemType(parseNullableType(resultMap.get(ITEM_TYPE)))
			       .build();
	}
	
	public List<Brand> retrieveBrandList(List<Map<String, Object>> resultList) {
		Brand brand = brandFromResultMap(resultList.get(0), ID);
		resultList.forEach(it-> {
			Item item = itemFromResultMap(it);
			brand.getItems().add(item);
		});
		return List.of(brand);
	}

	public Long retrieveId(Map<String, Object> row) {
		return parseNullableLong(row.get(ID));
	}

	private LocalDateTime parseNullableTime(Object obj) {
		return obj == null ? null : LocalDateTime.parse(obj.toString());
	}
}
