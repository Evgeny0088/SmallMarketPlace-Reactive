package com.marketplace.item.storage.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class Content<T> {
	
	private List<T> list;
	
}
