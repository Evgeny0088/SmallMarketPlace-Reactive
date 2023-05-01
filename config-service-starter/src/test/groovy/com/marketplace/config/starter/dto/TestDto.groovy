package com.marketplace.config.starter.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class TestDto {

    private String username
    private String password
    private String email
    private String roles
    private LocalDateTime dateTime

}
