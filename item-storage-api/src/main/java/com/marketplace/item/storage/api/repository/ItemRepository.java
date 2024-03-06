package com.marketplace.item.storage.api.repository;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.common.MessageConstants;
import com.marketplace.item.storage.api.dto.*;
import com.marketplace.item.storage.api.enums.ItemType;
import com.marketplace.item.storage.api.entity.Item;
import com.marketplace.item.storage.api.mapper.ItemRepositoryMapper;
import com.marketplace.item.storage.api.mapper.ItemServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemRepository implements Constants, MessageConstants {

	private final DatabaseClient client;
	private final ItemRepositoryMapper repositoryMapper;
	private final ItemServiceMapper itemServiceMapper;

	@Transactional
	public Mono<ItemUpdatePipeline> saveNewItem(ItemUpdatePipeline pipeline) {
		ItemRequest request = pipeline.getItemCreateRequest();
		return getParentIfExists(request)
				.handle((parent, sink)-> {
					if (!Objects.equals(parent.getBrand().getId(), request.getBrandId())) {
						CustomException ex = new CustomException(HttpStatus.BAD_REQUEST)
								.setDetails(String.format(ITEM_BRAND_MISMATCH, request.getBrandId(), parent.getId(), parent.getBrand().getId()));
						sink.error(ex);
					}
				})
				.then(saveItemOnRequest(pipeline))
				.flatMap(this::getItemDetailsWithBrandName);
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
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.NOT_FOUND)
								.setDetails(String.format(OBJECT_NOT_FOUND, ITEM_ID, request.getId())))
				)
				.map(id-> Long.parseLong(id.get(ID).toString()))
				.next();
	}

	@Transactional
	public Mono<ItemUpdatePipeline> getFullItemById(ItemUpdatePipeline pipeline) {
		long id = pipeline.getItemUpdateRequest().getId();
		return client.sql(
						"""
                        select 
                        b.brand_name, 
                        b.owner_id,
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
				)
				.bind(ID, id)
				.fetch()
				.all()
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.NOT_FOUND)
								.setDetails(String.format(OBJECT_NOT_FOUND, ITEM_ID, id)))
				)
				.buffer()
				.filter(Objects::nonNull)
				.doOnNext(rows-> rows.forEach(row-> log.info(String.format(OBJECT_FOUND, row.toString()))))
				.flatMapIterable(rows-> List.of(repositoryMapper.fetchFullItem(rows, id)))
				.map(item-> {
					pipeline.setItem(item);
					return pipeline;
				})
				.next();
	}

	public Mono<Item> getItemById(long id) {
		return client.sql(
						"""
                        select * from items where id=:id
                        """
				)
				.bind(ID, id)
				.map(repositoryMapper::itemFromRowResult)
				.one()
				.switchIfEmpty(
						Mono.error(new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(OBJECT_NOT_FOUND, ITEM_ID, id)))
				);
	}

	public Mono<ItemUpdatePipeline> saveItemOnRequest(ItemUpdatePipeline pipeline) {
		ItemRequest request = pipeline.getItemCreateRequest();
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
				.one()
				.map(item-> {
					pipeline.setItem(item);
					return pipeline;
				});
	}

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	public Mono<ItemUpdatePipeline> removeSoldItems(ItemUpdatePipeline pipeline) {
		long brandId = pipeline.getTransactionRequest().getItemDetails().getBrandId();
		long count = pipeline.getTransactionRequest().getItemDetails().getItemsCount();
		long parentId = pipeline.getTransactionRequest().getItemDetails().getItemId();
		return client.sql(
				"""
				with childrenTable as
         		(select id, row_number() over (order by id) as child_row_number from items where brand_id=:brand_id and item_type='ITEM' and parent_item=:parent_item)
				delete from items where id in
                (select id from childrenTable limit :items_count) and (select max(child_row_number) from childrenTable)>=:items_count
				returning id;
				"""
		)
				.bind(BRAND_ID, brandId)
				.bind(PARENT_ITEM, parentId)
				.bind(ITEMS_COUNT, count)
				.fetch()
				.all()
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.BAD_REQUEST)
								.setDetails(String.format(SELLING_ERROR, count)))
				)
				.then()
				.thenReturn(itemServiceMapper.updateItemDetailsCount(pipeline));
	}

	public Mono<ItemUpdatePipeline> getItemDetails(ItemUpdatePipeline pipeline) {
		long parentId = pipeline.getTransactionRequest().getItemDetails().getItemId();
		long brandId = pipeline.getTransactionRequest().getItemDetails().getBrandId();
		return client.sql(
				"""
				select
    			t1.id as parent_item,
    			(select count(id) as children from items where brand_id=:brand_id and parent_item=:parent_item and item_type='ITEM') as children,
    			t1.brand_id as brand_id
				from items as t1
				where t1.id=:parent_item and t1.brand_id = :brand_id group by t1.id;
				"""
		)
				.bind(PARENT_ITEM, parentId)
				.bind(BRAND_ID, brandId)
				.map(repositoryMapper::itemDetailsFromRowResult)
				.first()
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.NOT_FOUND)
								.setDetails(String.format(SELLING_ERROR_NOT_FOUND, parentId, brandId))))
				.map(details-> itemServiceMapper.addItemDetails(pipeline, details));
	}

	public Mono<ItemUpdatePipeline> getItemDetailsWithBrandName(ItemUpdatePipeline pipeline) {
		Item item = pipeline.getItem();
		if (item.getItemType().equals(ItemType.PACK)) {
			return Mono.just(pipeline);
		}
		long parentId = item.getParentItem().getId();
		long brandId = item.getBrand().getId();
		return client.sql(
						"""
                        select
                        t1.id as parent_item,
                        (select count(id) as children from items where brand_id=:brand_id and parent_item=:parent_item and item_type='ITEM') as children,
                        t1.brand_id as brand_id,
                        b.brand_name as brand_name
                        from items as t1 left join brands as b on t1.brand_id=b.id  
                        where t1.id=:parent_item and t1.brand_id = :brand_id group by t1.id, b.id;
                        """
				)
				.bind(PARENT_ITEM, parentId)
				.bind(BRAND_ID, brandId)
				.map(repositoryMapper::itemDetailsWithBrandFromRowResult)
				.first()
				.map(itemDetails -> {
					pipeline.setItemDetails(itemDetails);
					return pipeline;
				})
				.log();
	}

	private Mono<Item> getParentIfExists(ItemRequest request) {
		return request.getParentItem() != NULLABLE_ID
				? getItemById(request.getParentItem())
				: Mono.empty();
	}

	/*
	test methods
	*/

	public Mono<Item> getTestItem(Long id) {
		return client.sql(
				"""
				select * from items where id=:id
				"""
		)
				.bind(ID, id)
				.map(repositoryMapper::itemFromRowResult)
				.one()
				.switchIfEmpty(Mono.empty());
	}
}
