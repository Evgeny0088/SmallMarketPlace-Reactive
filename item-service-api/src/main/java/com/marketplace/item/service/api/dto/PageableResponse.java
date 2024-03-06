package com.marketplace.item.service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableResponse<T> {
	
	private int size;
	private Long nextEvaluatedKey;
	private Long prevEvaluatedKey;
	private Content<T> content;
	
}
