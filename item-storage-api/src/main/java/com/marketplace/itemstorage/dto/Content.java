package com.marketplace.itemstorage.dto;

import lombok.Data;

import java.util.List;

@Data
public class Content<T> {
	
	private List<T> list;
	
}
