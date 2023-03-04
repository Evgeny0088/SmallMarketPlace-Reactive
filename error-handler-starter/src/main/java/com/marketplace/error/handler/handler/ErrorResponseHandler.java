package com.marketplace.error.handler.handler;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.exception.lib.exception.CustomExceptionDto;
import com.marketplace.exception.lib.exception.ExceptionTitleMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorResponseHandler {

    private final ExceptionTitleMap exceptionTitleMap = new ExceptionTitleMap();

    public ResponseEntity<Object> buildErrorResponse(Exception e, HttpStatus statusCode, String specialDetail) {
        String detail;
        if (e instanceof CustomException) {
            detail = ((CustomException) e).getCustomExceptionDto().getDetails();
        }
        else if (!specialDetail.isEmpty()){
            detail = specialDetail;
        }
        else {
            detail = e.getMessage();
        }
        CustomExceptionDto body = CustomExceptionDto.builder().build()
                                            .customErrorBody(exceptionTitleMap.getTitle(statusCode.value()), detail, statusCode);
        return ResponseEntity.status(statusCode).body(body);
    }

}