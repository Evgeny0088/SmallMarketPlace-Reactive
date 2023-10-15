package com.marketplace.itemstorage.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.marketplace.itemstorage.common.Constants;
import com.marketplace.itemstorage.enums.ItemType;
import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.marketplace.itemstorage.common.Constants.ITEM_TABLE_NAME;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(ITEM_TABLE_NAME)
public class Item implements Serializable, Constants {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@EqualsAndHashCode.Include
	private Long id;
	
	@JoinColumn(name = BRAND_ID, nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private Brand brand;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = PARENT_ITEM)
	@JsonBackReference
	Item parentItem;
	
	@Column(name = ITEM_TYPE, nullable = false)
	@Enumerated(EnumType.STRING)
	ItemType itemType;
	
	@Column(name = CREATED_AT, nullable = false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = PARENT_ITEM, cascade = CascadeType.ALL)
	@Builder.Default
	Set<Item> childItems = new HashSet<>();
	
}
