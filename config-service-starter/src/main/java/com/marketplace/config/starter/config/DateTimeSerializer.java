package com.marketplace.config.starter.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.marketplace.config.starter.common.Constants;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
public class DateTimeSerializer extends JsonSerializer<LocalDateTime> implements Constants {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) {
        OffsetDateTime dt = OffsetDateTime.of(value, ZoneOffset.UTC);
        try {
            gen.writeString(DATE_TIME_UTC_FORMAT.format(dt));
        }
        catch (IOException e) {
            log.error(String.format(SERIALIZATION_ERROR, dt));
        }
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }

}
