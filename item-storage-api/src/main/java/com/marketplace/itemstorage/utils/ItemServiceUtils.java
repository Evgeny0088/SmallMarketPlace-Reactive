package com.marketplace.itemstorage.utils;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.common.MessageConstants;
import com.marketplace.itemstorage.dto.ItemRequest;
import com.marketplace.itemstorage.dto.PageableRequest;
import com.marketplace.itemstorage.enums.ItemType;
import com.marketplace.itemstorage.enums.PageState;
import com.marketplace.itemstorage.properties.ItemProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ItemServiceUtils implements Constants, MessageConstants {
	
	private static final List<String> orders = List.of(DESC, ASC);
	
	public static ItemRequest verifyItemType(ItemRequest request) {
		request.setParentItem(request.getParentItem() == null ? NULLABLE_ID : request.getParentItem());
		
		if (request.getParentItem() == NULLABLE_ID && request.getItemType().equals(ItemType.ITEM)) {
			throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(ITEM_TYPE_ERROR);
		}
		return request;
	}
	
	public static Long parseNullableLong(Object obj) {
		return obj == null ? null : Long.parseLong(obj.toString());
	}
	
	public static ItemType parseNullableType(Object obj) {
		return obj == null ? null : ItemType.valueOf(obj.toString());
	}
	
	public static PageableRequest setupPageRequest(PageableRequest request, ItemProperties itemProperties) {
		String order = StringUtils.isBlank(request.getOrder()) ? DESC : request.getOrder();
		order = orders.contains(order) ? order : DESC;
		Integer size = request.getSize();
		size = ObjectUtils.isEmpty(size) ? itemProperties.getSize() : size;
		request.setSize(size);
		request.setOrder(order);
		return definePageState(request);
	}
	
	public static PageableRequest definePageState(PageableRequest request) {
		Long prevKey = request.getPrevEvaluatedKey();
		Long nextKey = request.getNextEvaluatedKey();
		if (ObjectUtils.isEmpty(prevKey) && ObjectUtils.isEmpty(nextKey)) {
			request.setPageState(PageState.FIRST_PAGE);
		}
		else if (ObjectUtils.isEmpty(nextKey)) {
			request.setPageState(PageState.PREV_KEY);
		}
		else if (ObjectUtils.isEmpty(prevKey)) {
			request.setPageState(PageState.NEXT_KEY);
		}
		return request;
	}
}
