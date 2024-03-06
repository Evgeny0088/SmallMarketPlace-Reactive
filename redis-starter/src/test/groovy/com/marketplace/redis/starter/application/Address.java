package com.marketplace.redis.starter.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private LocalDateTime registeredAt;

}
