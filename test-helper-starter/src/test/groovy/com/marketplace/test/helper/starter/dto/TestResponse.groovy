package com.marketplace.test.helper.starter.dto

import lombok.Data

@Data
class TestResponse <T> {

    String httpCode = 200
    String responseMessage = "Successful response!..."
    T responseBody
}
