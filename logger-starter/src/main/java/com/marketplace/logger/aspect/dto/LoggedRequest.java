package com.marketplace.logger.aspect.dto;

import com.marketplace.logger.aspect.common.Constants;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class LoggedRequest implements Constants {

    private String httpMethod = UNKNOWN_HTTP_METHOD;
    private String path = EMPTY;
    private Map<String, Object> arguments = new HashMap<>();

}
