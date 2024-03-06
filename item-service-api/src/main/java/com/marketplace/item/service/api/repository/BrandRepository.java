package com.marketplace.item.service.api.repository;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.common.MessageConstants;
import com.marketplace.item.service.api.dto.PageableRequest;
import com.marketplace.item.service.api.entity.Brand;
import com.marketplace.item.service.api.mapper.BrandRepositoryMapper;
import com.marketplace.item.service.api.dto.BrandRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
	public Mono<Brand> saveBrandOnRequest(BrandRequest request, String ownerId) {
		String name = request.getBrandName();
		return client.sql(
						"""
                        insert into brands(brand_name, owner_id) values (:brand_name, :owner_id) returning brands.id;
                        """
				)
				.bind(BRAND_NAME, name)
				.bind(OWNER_ID, ownerId)
				.map(row-> repositoryMapper.fromCreate(row, request, ownerId))
				.one();
	}

	@Transactional
	public Mono<Brand> updateBrand(BrandRequest request, String ownerId) {
		String name = request.getBrandName();
		String updatedName = request.getUpdatedName();
		if (StringUtils.isBlank(updatedName)) {
			throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(FIELD_REQUEST_ERROR, UPDATED_NAME_FIELD));
		}
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
				.map(brand -> repositoryMapper.onUpdateName(brand, ownerId, updatedName))
				.flatMap(this::saveBrand);
	}

	@Transactional
	public Mono<Long> deleteByName(String brandName, String ownerId) {
		return client.sql(
						"""
                        delete from brands as b where b.brand_name=:brand_name and owner_id=:owner_id returning id;
                        """
				)
				.bind(BRAND_NAME, brandName)
				.bind(OWNER_ID, ownerId)
				.fetch()
				.one()
				.switchIfEmpty(Mono.error(
						new CustomException(HttpStatus.NOT_FOUND)
								.setDetails(String.format(DELETE_ONLY_BY_OWNER, ownerId))
				))
				.map(repositoryMapper::retrieveId);
	}

	@Transactional(readOnly = true)
	public Flux<Brand> getBrandFirstPage(PageableRequest request) {
		return client.sql(
						"""
                        select
                            b.id as id,
                            b.owner_id as owner_id,
                            b.brand_name as brand_name,
                            b.created_at as brand_created_at,
                            b.updated_at as brand_updated_at,
                            i.id as item_id,
                            i.item_type as item_type,
                            i.created_at as item_created_at
                        from brands b left join items i on b.id=i.brand_id limit :limit
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
                            b.owner_id as owner_id,
                            b.brand_name as brand_name,
                            b.created_at as brand_created_at,
                            b.updated_at as brand_updated_at,
                            i.id as item_id,
                            i.item_type as item_type,
                            i.created_at as item_created_at
                        from brands b left join items i on b.id=i.brand_id where b.id < :id limit :limit
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
                            b.owner_id as owner_id,
                            b.brand_name as brand_name,
                            b.created_at as brand_created_at,
                            b.updated_at as brand_updated_at,
                            i.id as item_id,
                            i.item_type as item_type,
                            i.created_at as item_created_at
                        from brands b left join items i on b.id=i.brand_id where b.id > :id limit :limit
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

	/*
	test methods
	*/
	public Mono<Void> deleteAll() {
		return client.sql(
				"""
    			delete from brands
				"""
		).then();
	}

	public Mono<Brand> saveBrandTest(BrandRequest request, String ownerId) {
		return client.sql(
						"""
                        insert into brands(id, brand_name, owner_id) values (:id, :brand_name, :owner_id) returning brands.id;
                        """
				)
				.bind(ID, request.getId())
				.bind(BRAND_NAME, request.getBrandName())
				.bind(OWNER_ID, ownerId)
				.map(row-> repositoryMapper.fromCreate(row, request, ownerId))
				.one();
	}

	public Mono<Brand> findBrandByName(String name) {
		return client.sql(
				"""
    			select * from brands where brand_name = :brand_name
				"""
		)
				.bind(BRAND_NAME, name)
				.map(repositoryMapper::fromRowResult)
				.one()
				.switchIfEmpty(Mono.empty());
	}
}