package com.marketplace.kafka.starter.application;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {

    private String id;
    private String username;
    private LocalDateTime createdAt;

}
