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
public class ItemResponse {
	
	private String message;
	private Long id;
	private Long parentId;
	private String itemType;
	private Long brandId;
	private String brandName;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	
}
