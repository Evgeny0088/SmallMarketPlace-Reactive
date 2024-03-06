package com.marketplace.item.service.api.mapper;

import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.dto.ItemDetails;
import com.marketplace.item.service.api.dto.ItemUpdatePipeline;
import com.marketplace.item.service.api.entity.Brand;
import com.marketplace.item.service.api.entity.Item;
import com.marketplace.item.service.api.enums.ItemType;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.marketplace.item.service.api.utils.ItemServiceUtils.*;

@Component
@RequiredArgsConstructor
public class ItemRepositoryMapper implements Constants {

	private final BrandRepositoryMapper brandRepositoryMapper;

	public Item fromCreate(Row row, Long brandId, Long parentId, String type) {
		return Item.builder()
			       .id(parseNullableLong(row.get(ID)))
			       .brand(Brand.builder().id(brandId).build())
			       .parentItem(Item.builder().id(parentId == NULLABLE_ID ? null : parentId).build())
			       .itemType(ItemType.valueOf(type))
			       .createdAt(LocalDateTime.now())
			       .build();
	}

	public Item itemFromRowResult(Row row) {
		return Item.builder()
				.id(parseNullableLong(row.get(ID)))
				.itemType(ItemType.valueOf(parseNullableString(row.get(ITEM_TYPE))))
				.brand(Brand.builder().id(parseNullableLong(row.get(BRAND_ID))).build())
				.build();
	}

	public Item itemFromRowResultTest(Map<String, Object> row) {
		return Item.builder()
				.id(parseNullableLong(row.get(ID)))
				.brand(Brand.builder().id(parseNullableLong(row.get(BRAND_ID))).build())
				.build();
	}

	public ItemDetails itemDetailsFromRowResult(Row row) {
		return ItemDetails.builder()
				.itemId(parseNullableLong(row.get(PARENT_ITEM)))
				.brandId(parseNullableLong(row.get(BRAND_ID)))
				.itemsCount(parseNullableLong(row.get(CHILDREN)))
				.build();
	}

	public ItemDetails itemDetailsWithBrandFromRowResult(Row row) {
		return ItemDetails.builder()
				.itemId(parseNullableLong(row.get(PARENT_ITEM)))
				.brandName(parseNullableString(row.get(BRAND_NAME)))
				.brandId(parseNullableLong(row.get(BRAND_ID)))
				.itemsCount(parseNullableLong(row.get(CHILDREN)))
				.build();
	}

	public Item fetchFullItem(List<Map<String, Object>> rows, long id) {
		Brand itemBrand = brandRepositoryMapper.brandFromResultMap(rows.get(0), BRAND_ID);
		Item item = Item.builder()
				.id(id)
				.brand(itemBrand)
				.childItems(new HashSet<>())
				.build();

		rows.forEach(obj-> {
			if (parseNullableLong(obj.get(ID)) == id) {
				Long parentId = parseNullableLong(obj.get(PARENT_ITEM));
				item.setParentItem(
						parentId == null
								? null
								: Item.builder()
								.id(parseNullableLong(obj.get(PARENT_ITEM)))
								.brand(itemBrand)
								.build()
				);
				item.setItemType(ItemType.valueOf(obj.get(ITEM_TYPE).toString()));
				item.setCreatedAt(LocalDateTime.parse(obj.get(ITEM_CREATED_AT).toString()));
			}
			else {
				Item child = Item.builder()
						.id(parseNullableLong(obj.get(ID)))
						.parentItem(item)
						.brand(itemBrand)
						.createdAt(LocalDateTime.parse(obj.get(ITEM_CREATED_AT).toString()))
						.build();
				item.getChildItems().add(child);
			}
		});
		return item;
	}

	public ItemDetails buildEmptyItemDetails(ItemUpdatePipeline pipeline) {
		return ItemDetails.builder()
				.brandId(pipeline.getTransactionRequest().getItemDetails().getBrandId())
				.itemId(pipeline.getTransactionRequest().getItemDetails().getItemId())
				.itemsCount(0L)
				.build();
	}
}
