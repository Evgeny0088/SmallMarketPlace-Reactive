package com.marketplace.gateway.api.handler;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.exception.lib.exception.CustomExceptionDto;
import com.marketplace.gateway.api.common.Constants;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultExceptionHandler extends DefaultErrorWebExceptionHandler implements Constants {

    public DefaultExceptionHandler(ErrorAttributes errorAttributes,
                                   WebProperties.Resources resources,
                                   ErrorProperties errorProperties,
                                   ApplicationContext applicationContext) {
        super(errorAttributes, resources, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        int status;
        Map<String, Object> responseBodyMap = new LinkedHashMap<>();
        Throwable throwable = getError(request);

        if (throwable instanceof CustomException exception) {
            CustomExceptionDto errorBody = exception.getCustomExceptionDto();
            status = errorBody.getErrorCode();
            responseBodyMap.put(TITLE, errorBody.getTitle());
            responseBodyMap.put(CODE, errorBody.getErrorCode());
            responseBodyMap.put(DETAIL, errorBody.getDetails());
        }
        else if (throwable instanceof HttpClientErrorException exception) {
            status = exception.getStatusCode().value();
            responseBodyMap.put(TITLE, exception.getMessage());
            responseBodyMap.put(CODE, status);
        }
        else if (throwable instanceof Exception exception){
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            responseBodyMap.put(TITLE, UNEXPECTED_ERROR);
            responseBodyMap.put(CODE, status);
            responseBodyMap.put(DETAIL, exception.getMessage());
        }
        else {
            return super.renderErrorResponse(request);
        }
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(responseBodyMap));
    }
}
