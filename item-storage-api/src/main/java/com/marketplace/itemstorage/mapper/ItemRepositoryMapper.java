package com.marketplace.itemstorage.mapper;

import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.entity.Brand;
import com.marketplace.itemstorage.entity.Item;
import com.marketplace.itemstorage.enums.ItemType;
import com.marketplace.itemstorage.repository.BrandRepository;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.marketplace.itemstorage.utils.ItemServiceUtils.parseNullableLong;

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

	public Item fromRowResult(Row row) {
		return Item.builder()
				.id(parseNullableLong(row.get(ID)))
				.brand(Brand.builder().id(parseNullableLong(row.get(BRAND_ID))).build())
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
				item.setParentItem(
						Item.builder()
						.id(parseNullableLong(obj.get(PARENT_ITEM)))
						.brand(itemBrand)
						.build());
				item.setItemType(ItemType.valueOf(obj.get(ITEM_TYPE).toString()));
				item.setCreatedAt(LocalDateTime.parse(obj.get(ITEM_CREATED_AT).toString()));
			}
			else {
				Item child = Item.builder()
						.id(parseNullableLong(obj.get(ID)))
						.parentItem(Item.builder().id(item.getId()).build())
						.brand(itemBrand)
						.createdAt(LocalDateTime.parse(obj.get(ITEM_CREATED_AT).toString()))
						.build();
				item.getChildItems().add(child);
			}
		});
		return item;
	}
}
