package com.marketplace.item.service.api.service;

import com.marketplace.item.service.api.common.Constants;
import com.marketplace.item.service.api.common.MessageConstants;
import com.marketplace.item.service.api.dto.*;
import com.marketplace.item.service.api.entity.Brand;
import com.marketplace.item.service.api.enums.PageState;
import com.marketplace.item.service.api.mapper.BrandServiceMapper;
import com.marketplace.item.service.api.properties.ItemProperties;
import com.marketplace.item.service.api.repository.BrandRepository;
import com.marketplace.item.service.api.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import static com.marketplace.item.service.api.utils.ItemServiceUtils.setupPageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService implements Constants, MessageConstants {

	private final BrandRepository brandRepository;
	private final BrandServiceMapper brandMapper;
	private final ItemProperties itemProperties;
	private final RedisRepository redisRepository;
	private final Map<PageState, Function<PageableRequest, Flux<Brand>>> brandPageCallableMap;

	public Mono<BrandResponse> createBrand(BrandRequest request, String ownerId) {
		return brandRepository.saveBrandOnRequest(request, ownerId)
				.map (brand -> brandMapper.fromBrandOnCreate(brand, OBJECT_CREATED))
				.onErrorMap(Exceptions::propagate)
				.log();
	}

	public Mono<BrandResponse> updateBrand(BrandRequest request, String ownerId) {
		return brandRepository.updateBrand(request, ownerId)
				.map(brand -> brandMapper.fromBrandOnUpdate(brand, String.format(OBJECT_UPDATED, brand.getId())))
				.onErrorMap(Exceptions::propagate)
				.log();
	}

	public Mono<BrandResponse> deleteBrand(BrandRequest request, String ownerId) {
		return brandRepository.deleteByName(request.getBrandName(), ownerId)
				.flatMap(redisRepository::cleanupCacheOnDeletedBrand)
				.thenReturn(brandMapper.withMessageOnly(request.getBrandName()))
				.onErrorMap(Exceptions::propagate)
				.log();
	}

	public Mono<PageableResponse<Brand>> getBrandPage(PageableRequest pageableRequest) {
		return Mono.fromCallable(()-> setupPageRequest(pageableRequest, itemProperties))
				.flatMap (
						request-> brandPageCallableMap.get(request.getPageState()).apply(request).collectList()
								.map(list-> {
									boolean empty = list.isEmpty();
									Long prevKey = !empty ? list.get(0).getId() : null;
									Long lastKey = !empty ? list.get(list.size() -1).getId() : null;
									Content<Brand> content = new Content<>();
									list =
											pageableRequest.getOrder().equals(DESC)
													? list.stream().sorted(Comparator.comparing(Brand::getId).reversed()).toList()
													: list;
									content.setList(list);
									return PageableResponse.<Brand>builder()
											.content(content)
											.nextEvaluatedKey(lastKey)
											.prevEvaluatedKey(prevKey)
											.size(list.size())
											.build();
								}))
				.log();
	}
}
