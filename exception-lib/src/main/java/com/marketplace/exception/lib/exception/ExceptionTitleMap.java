package com.marketplace.exception.lib.exception;

import com.marketplace.exception.lib.common.Constants;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Data
@Configuration
public class ExceptionTitleMap implements Constants {

    private final Map<Integer, String> errorTitleMap = Map.ofEntries(
            Map.entry(400, BAD_REQUEST_TITLE),
            Map.entry(401, AUTHENTICATION_REQUEST_TITLE),
            Map.entry(403, FORBIDDEN_REQUEST_TITLE),
            Map.entry(404, NOT_FOUND_TITLE),
            Map.entry(500, INTERNAL_SERVER_ERROR_TITLE),
            Map.entry(503, SERVICE_NOT_AVAILABLE)
    );

    public String getTitle(int status){
        return errorTitleMap.getOrDefault(status, ERROR_MESSAGE_UNKNOWN);
    }
}
