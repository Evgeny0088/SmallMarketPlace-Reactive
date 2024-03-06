package com.marketplace.item.storage.api.controller;

import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.dto.BrandRequest;
import com.marketplace.item.storage.api.dto.BrandResponse;
import com.marketplace.item.storage.api.dto.PageableRequest;
import com.marketplace.item.storage.api.dto.PageableResponse;
import com.marketplace.item.storage.api.entity.Brand;
import com.marketplace.item.storage.api.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.marketplace.item.storage.api.common.Constants.DELETE;
import static com.marketplace.item.storage.api.common.Constants.GET_ALL_BRANDS;

@RestController
@RequestMapping(path = Constants.API_PREFIX_BRAND)
@RequiredArgsConstructor
@Validated
public class BrandController {
	
	private final BrandService brandService;
	
	@PostMapping
	public ResponseEntity<Mono<BrandResponse>> createBrand(@RequestHeader(Constants.HEADER_PROFILE_ID) String profileId,
														   @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.createBrand(request, profileId));
	}
	
	@PutMapping
	public ResponseEntity<Mono<BrandResponse>> updateBrand(@RequestHeader(Constants.HEADER_PROFILE_ID) String profileId,
	                                                       @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.updateBrand(request, profileId));
	}
	
	@PostMapping(DELETE)
	public ResponseEntity<Mono<BrandResponse>> deleteBrand(@RequestHeader(Constants.HEADER_PROFILE_ID) String profileId,
	                                                @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.deleteBrand(request, profileId));
	}

	@GetMapping(GET_ALL_BRANDS)
	public ResponseEntity<Mono<PageableResponse<Brand>>> getAllBrands(PageableRequest pageableRequest) {
		return ResponseEntity.ok(brandService.getBrandPage(pageableRequest));
	}
}
