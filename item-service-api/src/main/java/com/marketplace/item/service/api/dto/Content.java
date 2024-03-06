package com.marketplace.item.service.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class Content<T> {
	
	private List<T> list;
	
}
