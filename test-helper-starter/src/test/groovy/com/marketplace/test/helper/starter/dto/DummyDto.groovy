package com.marketplace.test.helper.starter.dto

import lombok.Data

@Data
class DummyDto {

    String username = "username"
    String email = "test@gmail.com"
    String roles = "USER, ADMIN"
    InnerDummyClass innerDummyClass = new InnerDummyClass()

    @Data
    private static class InnerDummyClass {
        String id = "a68dbcd2-2d54-4aa7-a6e4-0365253ae19b"
        int arg1 = 100
        String arg2 = "any arg"
        boolean arg3 = true
    }
}