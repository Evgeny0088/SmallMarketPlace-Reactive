package com.marketplace.kafka.starter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.kafka.starter.Constants;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class StarterJsonDeserializer<T> implements Deserializer<T>, Constants {

    private final String encoding = StandardCharsets.UTF_8.name();
    private final ObjectMapper mapper;
    private final Class<T> type;

    public StarterJsonDeserializer (ObjectMapper objectMapper, Class<T> type) {
        this.mapper = objectMapper;
        this.type = type;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            var valueAsString = new String(data, encoding);
            return mapper.readValue(valueAsString, type);
        }
        catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to StringValue", e);
        }
    }
}
