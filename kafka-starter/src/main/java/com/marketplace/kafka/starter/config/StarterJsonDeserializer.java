package com.marketplace.kafka.starter.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.kafka.starter.Constants;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StarterJsonDeserializer<T> implements Deserializer<T>, Constants {

    private final String encoding = StandardCharsets.UTF_8.name();
    public static final String OBJECT_MAPPER = "objectMapper";
    public static final String TYPE_REFERENCE = "typeReference";
    private final ObjectMapper mapper;
    private final TypeReference<T> type;

    @SuppressWarnings("unchecked")
    public StarterJsonDeserializer(Map<String, ?> configs) {
        mapper = (ObjectMapper) configs.get(OBJECT_MAPPER);
        type = (TypeReference<T>) configs.get(TYPE_REFERENCE);
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
