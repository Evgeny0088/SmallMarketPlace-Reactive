package com.marketplace.kafka.starter.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {

    private String id;
    private String username;
    private Address address;
    private LocalDateTime createdAt;

}
