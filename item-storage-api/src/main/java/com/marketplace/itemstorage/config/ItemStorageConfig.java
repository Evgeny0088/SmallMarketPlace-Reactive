package com.marketplace.itemstorage.config;

import com.marketplace.itemstorage.dto.PageableRequest;
import com.marketplace.itemstorage.entity.Brand;
import com.marketplace.itemstorage.enums.PageState;
import com.marketplace.itemstorage.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class ItemStorageConfig {
	
	private final BrandRepository brandRepository;
	
	@Bean
	public Map<PageState, Function<PageableRequest, Flux<Brand>>> brandPageCallableMap() {
		Map<PageState, Function<PageableRequest, Flux<Brand>>> publishMap = new HashMap<>();
		publishMap.put(PageState.FIRST_PAGE, brandRepository::getBrandFirstPage);
		publishMap.put(PageState.PREV_KEY, brandRepository::getBrandPrevPage);
		publishMap.put(PageState.NEXT_KEY, brandRepository::getBrandNextPage);
		return publishMap;
	}
	
}
