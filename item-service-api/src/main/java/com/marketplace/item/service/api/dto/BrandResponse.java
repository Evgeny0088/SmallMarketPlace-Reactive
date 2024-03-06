package com.marketplace.item.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
	
	private String message;
	private Long id;
	private String brandName;
	private String ownerId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
