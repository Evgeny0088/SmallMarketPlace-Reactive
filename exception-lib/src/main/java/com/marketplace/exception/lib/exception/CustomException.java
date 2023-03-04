package com.marketplace.exception.lib.exception;

import com.marketplace.exception.lib.common.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException implements Constants {

    private HttpStatus httpStatus;
    private CustomExceptionDto customExceptionDto;
    private ExceptionTitleMap exceptionTitleMap = new ExceptionTitleMap();

    public CustomException(HttpStatus httpCode) {
        super(httpCode.name());
        this.httpStatus = httpCode;
        int statusCode = httpStatus.value();
        String title = exceptionTitleMap.getTitle(statusCode);
        customExceptionDto = CustomExceptionDto.builder()
                .title(title)
                .errorCode(statusCode)
                .build();
    }

    public CustomException setDetails(String details){
        customExceptionDto.setDetails(details);
        return this;
    }
}