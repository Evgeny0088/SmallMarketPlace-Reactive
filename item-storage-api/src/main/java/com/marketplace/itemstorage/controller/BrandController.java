package com.marketplace.itemstorage.controller;

import com.marketplace.itemstorage.dto.BrandRequest;
import com.marketplace.itemstorage.dto.BrandResponse;
import com.marketplace.itemstorage.dto.PageableRequest;
import com.marketplace.itemstorage.dto.PageableResponse;
import com.marketplace.itemstorage.entity.Brand;
import com.marketplace.itemstorage.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.marketplace.itemstorage.common.Constants.*;

@RestController
@RequestMapping(path = API_PREFIX_BRAND)
@RequiredArgsConstructor
@Validated
public class BrandController {
	
	private final BrandService brandService;
	
//	@Owner
	@PostMapping
	public ResponseEntity<Mono<BrandResponse>> createBrand(@RequestHeader(HEADER_PROFILE_ID) String profileId,
														   @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.createBrand(request));
	}
	
//	@Owner
	@PutMapping
	public ResponseEntity<Mono<BrandResponse>> updateBrand(@RequestHeader(HEADER_PROFILE_ID) String profileId,
	                                                       @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.updateBrand(request));
	}
	
//	@Owner
	@DeleteMapping
	public ResponseEntity<Mono<BrandResponse>> deleteBrand(@RequestHeader(HEADER_PROFILE_ID) String profileId,
	                                                @Valid @RequestBody BrandRequest request) {
		return ResponseEntity.ok(brandService.deleteBrand(request));
	}
	
//	@Owner
//	@RolesRequired(roles = {"MANAGER", "ADMIN"})
	@GetMapping(path = GET_ALL_BRANDS)
	public ResponseEntity<Mono<PageableResponse<Brand>>> getAllBrands(@RequestHeader(HEADER_PROFILE_ID) String profileId,
	                                                                  @RequestHeader(ROLES) String roles,
	                                                                  PageableRequest pageableRequest) {
		return ResponseEntity.ok(brandService.getBrandPage(pageableRequest));
	}
	
}
