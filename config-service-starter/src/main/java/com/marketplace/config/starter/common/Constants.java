package com.marketplace.config.starter.common;

import java.time.format.DateTimeFormatter;

public interface Constants {

    String EMPTY = "";
    DateTimeFormatter DATE_TIME_UTC_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
    DateTimeFormatter DATE_TIME_UTC_FORMAT_DESERIALIZED = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
    String DESERIALIZATION_ERROR = "Failed to deserialize value = %s as LocalDateTime.";
    String SERIALIZATION_ERROR = "Failed to serialize value = %s as LocalDateTime";
}
