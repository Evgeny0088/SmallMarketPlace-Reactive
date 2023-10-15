package com.marketplace.itemstorage.mapper;

import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.entity.Brand;
import com.marketplace.itemstorage.entity.Item;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.marketplace.itemstorage.utils.ItemServiceUtils.parseNullableLong;
import static com.marketplace.itemstorage.utils.ItemServiceUtils.parseNullableType;

@Component
public class BrandRepositoryMapper implements Constants {
	
	public Brand fromCreate(Row row, String name) {
		return Brand.builder()
			       .id(parseNullableLong(row.get(ID)))
			       .brandName(name)
			       .createdAt(LocalDateTime.now())
			       .build();
	}
	
	public Brand onUpdateName(Brand brand, String updatedName) {
		brand.setBrandName(updatedName);
		brand.setUpdatedAt(LocalDateTime.now());
		return brand;
	}
	
	public Brand fromRowResult(Row row) {
		return Brand.builder()
				.id(row.get(ID, Long.class))
				.brandName(row.get(BRAND_NAME, String.class))
				.createdAt(row.get(CREATED_AT, LocalDateTime.class))
				.updatedAt(row.get(UPDATED_AT, LocalDateTime.class))
				.build();
	}
	
	public Brand brandFromResultMap(Map<String, Object> resultMap, String idName) {
		return Brand.builder()
			       .id(Long.parseLong(resultMap.get(idName).toString()))
			       .brandName(resultMap.get(BRAND_NAME).toString())
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
	
	private LocalDateTime parseNullableTime(Object obj) {
		return obj == null ? null : LocalDateTime.parse(obj.toString());
	}
}
