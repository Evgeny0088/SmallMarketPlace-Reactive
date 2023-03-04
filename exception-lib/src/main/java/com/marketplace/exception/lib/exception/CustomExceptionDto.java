package com.marketplace.exception.lib.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class CustomExceptionDto {

    String title;
    int errorCode;
    String details;

    public CustomExceptionDto customErrorBody(String title, String details, HttpStatus status){
        return CustomExceptionDto.builder()
                .title(title)
                .errorCode(status.value())
                .details(details)
                .build();
    }
}
