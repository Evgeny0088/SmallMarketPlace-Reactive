package com.marketplace.logger.aspect.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.marketplace.exception.lib.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature;

@Configuration
@RequiredArgsConstructor
public class LoggerConfig {

    @Bean
    Map<Class<?>, HttpMethod> restMappingMap() {
        return Map.of(
                GetMapping.class, HttpMethod.GET,
                PostMapping.class, HttpMethod.POST,
                PutMapping.class, HttpMethod.PUT,
                PatchMapping.class, HttpMethod.PATCH,
                DeleteMapping.class, HttpMethod.DELETE
        );
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public JsonGenerator jsonGenerator(){
        try {
            JsonFactory factory = new JsonFactory();
            StringWriter sw = new StringWriter();
            JsonGenerator generator = factory.createGenerator(sw);
            generator.configure(Feature.IGNORE_UNKNOWN, true);
            generator.useDefaultPrettyPrinter();
            return generator;
        }
        catch (IOException e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR).setDetails(e.getMessage());
        }
    }
}
