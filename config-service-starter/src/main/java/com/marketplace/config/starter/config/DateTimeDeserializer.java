package com.marketplace.config.starter.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.marketplace.config.starter.common.Constants;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Slf4j
public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> implements Constants {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
        String source = EMPTY;
        OffsetDateTime offsetDateTime;
        try {
            source = p.readValueAs(String.class);
            return LocalDateTime.parse(source, DATE_TIME_UTC_FORMAT);
        }
        catch (Exception e) {
            try {
                offsetDateTime = OffsetDateTime.parse(source);
            }
            catch (Exception timeZoneException) {
                log.warn(String.format(DESERIALIZATION_ERROR, source));
                offsetDateTime = OffsetDateTime.parse(source, DATE_TIME_UTC_FORMAT_DESERIALIZED);
            }
            return offsetDateTime.atZoneSameInstant(ZoneId.of("Z")).toLocalDateTime();
        }
    }

    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }
}