package com.marketplace.item.storage.api.config;

import com.marketplace.item.storage.api.dto.PageableRequest;
import com.marketplace.item.storage.api.entity.Brand;
import com.marketplace.item.storage.api.enums.PageState;
import com.marketplace.item.storage.api.repository.BrandRepository;
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
