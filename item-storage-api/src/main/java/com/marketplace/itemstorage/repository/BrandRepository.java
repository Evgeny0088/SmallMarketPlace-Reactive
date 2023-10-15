package com.marketplace.itemstorage.repository;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.common.MessageConstants;
import com.marketplace.itemstorage.dto.BrandRequest;
import com.marketplace.itemstorage.dto.PageableRequest;
import com.marketplace.itemstorage.entity.Brand;
import com.marketplace.itemstorage.mapper.BrandRepositoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BrandRepository implements Constants, MessageConstants {

	private final DatabaseClient client;
	private final BrandRepositoryMapper repositoryMapper;
	
	@Transactional
	public Mono<Brand> saveBrand(Brand brand) {
		return client.sql(
			"""
			update brands set brand_name=:brand_name, updated_at=:updated_at where id=:id
			"""
		)
			       .bind(ID, brand.getId())
			       .bind(BRAND_NAME, brand.getBrandName())
			       .bind(UPDATED_AT, brand.getUpdatedAt())
			       .then().thenReturn(brand);
	}
	
	@Transactional
	public Mono<Brand> saveBrandOnRequest(BrandRequest request) {
		String name = request.getBrandName();
		return client.sql(
			"""
                        insert into brands(brand_name) values (:brand_name) returning brands.id;
                        """
			)
			       .bind(BRAND_NAME, name)
			       .map(row-> repositoryMapper.fromCreate(row, request.getBrandName()))
			       .one();
	}
	
	@Transactional
	public Mono<Brand> updateBrand(BrandRequest request) {
		String name = request.getBrandName();
		String updatedName = request.getUpdatedName();
		return client.sql(
			"""
			select * from brands as b where b.brand_name=:brand_name;
			"""
		)
			       .bind(BRAND_NAME, name)
			       .map(repositoryMapper::fromRowResult)
			       .one()
			       .switchIfEmpty(Mono.error(
				       new CustomException(HttpStatus.NOT_FOUND)
					       .setDetails(String.format(OBJECT_NOT_FOUND, BRAND_NAME, name))))
			       .map(brand -> repositoryMapper.onUpdateName(brand, updatedName))
			       .flatMap(this::saveBrand);
	}
	
	@Transactional
	public Mono<Void> deleteByName(String brandName) {
		return client.sql(
			"""
			delete from brands as b where b.brand_name=:brand_name
			"""
		)
			       .bind(BRAND_NAME, brandName)
			       .then();
	}
	
	@Transactional(readOnly = true)
	public Flux<Brand> getBrandFirstPage(PageableRequest request) {
		return client.sql(
			"""
			select
				b.id as id,
                                b.brand_name as brand_name,
                                b.created_at as brand_created_at,
                                b.updated_at as brand_updated_at,
                                i.id as item_id,
                                i.item_type as item_type,
                                i.created_at as item_created_at
				from brands b left join items i on b.id=i.brand_id limit :limit;
                        """
		)
			       .bind(LIMIT, request.getSize())
			       .fetch()
			       .all()
			       .bufferUntilChanged(item-> item.get(ID))
			       .filter(Objects::nonNull)
			       .flatMapIterable(repositoryMapper::retrieveBrandList)
			       .log();
	}
	
	@Transactional(readOnly = true)
	public Flux<Brand> getBrandPrevPage(PageableRequest request) {
		return client.sql(
				"""
				select
                                        b.id as id,
                                        b.brand_name as brand_name,
                                        b.created_at as brand_created_at,
                                        b.updated_at as brand_updated_at,
                                        i.id as item_id,
                                        i.item_type as item_type,
                                        i.created_at as item_created_at
					from brands b left join items i on b.id=i.brand_id where b.id < :id limit :limit;
				"""
			)
			       .bind(ID, request.getPrevEvaluatedKey())
			       .bind(LIMIT, request.getSize())
			       .fetch()
			       .all()
			       .bufferUntilChanged(i-> i.get(ID))
			       .filter(Objects::nonNull)
			       .flatMapIterable(repositoryMapper::retrieveBrandList)
			       .log();
	}
	
	@Transactional(readOnly = true)
	public Flux<Brand> getBrandNextPage(PageableRequest request) {
		return client.sql (
				"""
				select
                                        b.id as id,
                                        b.brand_name as brand_name,
                                        b.created_at as brand_created_at,
                                        b.updated_at as brand_updated_at,
                                        i.id as item_id,
                                        i.item_type as item_type,
                                        i.created_at as item_created_at
					from brands b left join items i on b.id=i.brand_id where b.id > :id limit :limit;
				"""
			)
			       .bind(ID, request.getNextEvaluatedKey())
			       .bind(LIMIT, request.getSize())
			       .fetch()
			       .all()
			       .bufferUntilChanged(i-> i.get(ID))
			       .filter(Objects::nonNull)
			       .flatMapIterable(repositoryMapper::retrieveBrandList)
			       .log();
	}
}