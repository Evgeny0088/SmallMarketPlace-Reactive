package com.marketplace.itemstorage.repository;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.common.MessageConstants;
import com.marketplace.itemstorage.dto.ItemRequest;
import com.marketplace.itemstorage.dto.ItemUpdateRequest;
import com.marketplace.itemstorage.entity.Item;
import com.marketplace.itemstorage.enums.ItemType;
import com.marketplace.itemstorage.mapper.ItemRepositoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepository implements Constants, MessageConstants {

	private final DatabaseClient client;
	private final ItemRepositoryMapper repositoryMapper;

	@Transactional
	public Mono<Item> saveNewItem(ItemRequest request) {
		return getParentIfExists(request)
				.handle((parent, sink)-> {
					if (!Objects.equals(parent.getBrand().getId(), request.getBrandId())) {
						CustomException ex = new CustomException(HttpStatus.BAD_REQUEST)
								.setDetails(String.format(ITEM_BRAND_MISMATCH, request.getBrandId(), parent.getId(), parent.getBrand().getId()));
						sink.error(ex);
					}
				})
				.then(saveItemOnRequest(request));
	}

	@Transactional
	public Mono<Item> saveItem(Item item) {
		return client.sql(
						"""
                        update items set item_type=:item_type, brand_id=:brand_id where items.id=:id
                        """
				)
				.bind(ITEM_TYPE, item.getItemType().name())
				.bind(BRAND_ID, item.getBrand().getId())
				.bind(ID, item.getId())
				.then()
				.then(client.sql(
						"""
                       	update items set brand_id=:brand_id where items.parent_item=:id
						"""
				)
								.bind(BRAND_ID, item.getBrand().getId())
								.bind(ID, item.getId()).then())
				.log()
				.thenReturn(item);
	}

	@Transactional
	public Mono<Item> updateItem(ItemUpdateRequest request) {
		return getFullItemById(request.getId())
				.<Item>handle((item, sink) -> {
					Long brandId = request.getBrandId();
					ItemType type = request.getItemType();

					if (ObjectUtils.isNotEmpty(brandId)) {
						item.getBrand().setId(brandId);
						item.getChildItems().forEach(i-> i.getBrand().setId(brandId));
					}

					if (type == ItemType.ITEM && !item.getChildItems().isEmpty()) {
						sink.error(new CustomException(HttpStatus.BAD_REQUEST)
								.setDetails(ITEM_TYPE_ERROR_1));
						return;
					}
					else item.setItemType(type == null ? item.getItemType() : type);
					sink.next(item);
				})
				.log()
				.flatMap(this::saveItem);
	}

	@Transactional
	public Mono<Long> deleteItemById(ItemUpdateRequest request) {
		return client.sql(
				"""
				delete from items as i where i.id=:id or i.parent_item=:id returning id
				"""
		)
				.bind(ID, request.getId())
				.fetch()
				.all()
				.filter(map-> map.get(ID).equals(request.getId()))
				.map(id-> Long.parseLong(id.get(ID).toString()))
				.next();
	}

	@Transactional
	public Mono<Item> getFullItemById(Long id) {
		return client.sql(
						"""
                        select 
                        b.brand_name, 
                        b.created_at as brand_created_at, 
                        b.updated_at as brand_updated_at, 
                        i.brand_id as brand_id, 
                        i.id, 
                        i.parent_item, 
                        i.item_type, 
                        i.created_at as item_created_at
                        from items i left join brands b on i.brand_id=b.id 
                        where i.id=:id or i.parent_item=:id
                        """
				).bind(ID, id)
				.fetch()
				.all()
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.NOT_FOUND)
								.setDetails(String.format(OBJECT_NOT_FOUND, ID, id)))
				)
				.buffer()
				.filter(Objects::nonNull)
				.doOnNext(rows-> rows.forEach(row-> log.info(String.format(OBJECT_FOUND, row.toString()))))
				.flatMapIterable(rows-> List.of(repositoryMapper.fetchFullItem(rows, id)))
				.next();
	}

	public Mono<Item> getItemById(long id) {
		return client.sql(
						"""
                        select * from items where id=:id
                        """
				)
				.bind(ID, id)
				.map(repositoryMapper::fromRowResult)
				.one();
	}

	public Mono<Item> saveItemOnRequest(ItemRequest request) {
		Long brandId = request.getBrandId();
		Long parentId = request.getParentItem();
		String type = request.getItemType().name();
		return client.sql (
						"""
                        insert into items(brand_id, parent_item, item_type) values (:brand_id, nullif(:parent_item, -1), :item_type) returning items.id;
                        """
				)
				.bind(BRAND_ID, brandId)
				.bind(PARENT_ITEM, parentId)
				.bind(ITEM_TYPE, type)
				.map(row-> repositoryMapper.fromCreate(row, brandId, parentId, type))
				.one();
	}

	private Mono<Item> getParentIfExists(ItemRequest request) {
		return request.getParentItem() != NULLABLE_ID
				? getItemById(request.getParentItem())
				: Mono.empty();
	}
}
