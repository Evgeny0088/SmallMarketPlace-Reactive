package com.marketplace.logger.aspect.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.logger.aspect.common.Constants;
import com.marketplace.logger.aspect.dto.LoggedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import reactor.core.CorePublisher;
import java.lang.reflect.Method;

import static com.marketplace.logger.aspect.common.ColorConstants.*;
import static com.marketplace.logger.aspect.utils.ColorUtils.coloredString;

@Slf4j
public class ResponseLogUtils implements Constants {

    public static Object tryToLogResponse(Method method, String logColor,
                                          ObjectMapper mapper,
                                          Object returnedObject,
                                          LoggedResponse<Object> loggedResponse) {
        try {
            if (method != null && method.getReturnType() == void.class) {
                logResponse(logColor, mapper, loggedResponse);
            }
            else if (returnedObject instanceof ResponseEntity<?> responseEntity) {
                loggedResponse.setHttpCode(String.valueOf(responseEntity.getStatusCodeValue()));
                if (responseEntity.getBody() instanceof CorePublisher<?> publisher){
                    logIfReactiveObject(publisher, loggedResponse, mapper, ANSI_GREEN);
                }
                else {
                    logIfResponseIsObject(responseEntity.getBody(), mapper, loggedResponse, logColor);
                }
            }
            else if (returnedObject instanceof CorePublisher<?> publisher) {
                logIfReactiveObject(publisher, loggedResponse, mapper, ANSI_GREEN);
            }
            else {
                logIfResponseIsObject(returnedObject, mapper, loggedResponse, logColor);
            }
        }
        catch (Exception e) {
            log.warn(String.format(ANSI_YELLOW + FAILED_TO_LOG_RESPONSE, loggedResponse.getPath() + ANSI_RESET));
            return returnedObject;
        }
        return returnedObject;
    }

    public static void logIfResponseIsObject(Object returnedObject, ObjectMapper mapper, LoggedResponse<Object> loggedResponse, String logColor) {
        loggedResponse.setResponseBody(returnedObject);
        logResponse(logColor, mapper, loggedResponse);
    }

    public static void logIfReactiveObject(CorePublisher<?> publisher, LoggedResponse<Object> loggedResponse, ObjectMapper mapper, String logColor) {
        loggedResponse.setResponseBody(String.format(REACTIVE_BODY_CONTENT, publisher.toString()));
        logResponse(logColor, mapper, loggedResponse);
    }

    private static void logResponse(String color, ObjectMapper mapper, LoggedResponse<?> loggedResponse) {
        try {
            log.info(color + String.format(RESPONSE, coloredString(mapper.writeValueAsString(loggedResponse), color)) + ANSI_RESET);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
