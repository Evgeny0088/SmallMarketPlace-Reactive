package com.marketplace.error.handler.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.error.handler.common.Constants;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.exception.lib.exception.CustomExceptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler implements Constants {

    private final ObjectMapper objectMapper;
    private final ErrorResponseHandler errorResponseHandler;

    @ExceptionHandler(MissingRequestValueException.class)
    public ResponseEntity<Object> handleException(MissingRequestValueException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e){
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleException(ConstraintViolationException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Object> handleException(WebExchangeBindException e) {
        String errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(Objects::nonNull)
                .map(this::getValidationErrorMessage)
                .collect(Collectors.joining(ERROR_PAIR_DELIMITER));
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler({HttpMessageConversionException.class, MissingServletRequestPartException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<Object> handleException(HttpMediaTypeException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleException(TypeMismatchException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST, EMPTY);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Object> handleException(WebClientRequestException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE, EMPTY);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleException(WebClientResponseException e) throws JsonProcessingException {
        CustomExceptionDto customExceptionDto = objectMapper.readValue(e.getResponseBodyAsString(), CustomExceptionDto.class);
        log.error(String.format(ERROR_LOG, e.getCause()));
        return ResponseEntity.status(customExceptionDto.getErrorCode()).body(customExceptionDto);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleException(CustomException e) {
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, e.getHttpStatus(), EMPTY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServerException(Exception e){
        log.error(String.format(ERROR_LOG, e.getCause()));
        return errorResponseHandler.buildErrorResponse(e, HttpStatus.BAD_REQUEST,
                e.getClass().getSimpleName().equals(ERROR_FROM_R2DBC_DUPLICATE_KEY) ? PROFILE_ALREADY_EXISTS : EMPTY);
    }

    @NotNull
    private String getValidationErrorMessage(@NotNull final ObjectError error) {
        StringBuilder errorBuilder = new StringBuilder();
        if (error instanceof FieldError fe) {
            errorBuilder.append(fe.getField()).append(FIELD_ERROR_DELIMITER);
        }
        errorBuilder.append(error.getDefaultMessage());
        return errorBuilder.toString();
    }
}
