package com.marketplace.authservice.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

import java.util.function.Consumer

class AuthServiceTestUtils {

    static Consumer<HttpHeaders> headersSetup(MediaType mediaType) {
        return (headers) -> headers.setContentType(mediaType)
    }
}
