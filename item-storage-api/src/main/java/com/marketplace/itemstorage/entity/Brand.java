package com.marketplace.itemstorage.entity;

import com.marketplace.itemstorage.common.Constants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.marketplace.itemstorage.common.Constants.BRAND_TABLE_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(BRAND_TABLE_NAME)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Brand implements Serializable, Comparable<Brand>, Constants {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(name = BRAND_NAME, nullable = false)
	private String brandName;
	
	@Column(name = CREATED_AT, nullable = false)
	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = UPDATED_AT)
	private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy = BRAND_ID, cascade = CascadeType.ALL)
	@Builder.Default
	List<Item> items = new ArrayList<>();
	
	@Override
	public int compareTo(Brand o) {
		return o.getId().compareTo(this.getId());
	}
}
