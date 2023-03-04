package com.marketplace.error.handler.decoder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.exception.lib.exception.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class BaseExceptionDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        Map<String, Object> body = readBody(response);
        return new CustomException(HttpStatus.valueOf(response.status()))
                                                        .setDetails(response.reason());
    }

    @SneakyThrows
    private Map<String, Object> readBody(Response response) {
        try(InputStream inputStream = response.body().asInputStream()){
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
