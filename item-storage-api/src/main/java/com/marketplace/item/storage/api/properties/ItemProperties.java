package com.marketplace.item.storage.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "item-properties")
public class ItemProperties {
	
	private int page = 0;
	private int size = 5;
	private int fetchFromCollectionSize = 5;
}
