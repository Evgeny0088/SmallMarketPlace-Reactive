package com.marketplace.kafka.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.kafka.starter.Constants;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class StarterJsonSerializer<T> implements Serializer<T>, Constants {

    private ObjectMapper mapper;

    public StarterJsonSerializer() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        mapper = (ObjectMapper) configs.get(OBJECT_MAPPER);
        if (mapper == null) {
            throw new IllegalArgumentException("config property OBJECT_MAPPER was not set");
        }
    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return mapper.writeValueAsBytes(data);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, T data) {
        return serialize(topic, data);
    }
}
