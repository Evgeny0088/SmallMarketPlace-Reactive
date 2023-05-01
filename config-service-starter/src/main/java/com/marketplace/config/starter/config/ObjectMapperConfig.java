package com.marketplace.config.starter.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class ObjectMapperConfig {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void setupObjectMapper() {
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.registerModule(this.dateTimeSerializationModule());
    }

    private SimpleModule dateTimeSerializationModule() {
        SimpleModule module = new SimpleModule("DATE-TIME-MODULE");
        module.addSerializer(LocalDateTime.class, new DateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new DateTimeDeserializer());
        return module;
    }

    public ObjectMapperConfig(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
