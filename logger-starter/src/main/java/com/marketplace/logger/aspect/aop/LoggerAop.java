package com.marketplace.logger.aspect.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.logger.aspect.dto.LoggedRequest;
import com.marketplace.logger.aspect.dto.LoggedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

import static com.marketplace.logger.aspect.common.ColorConstants.*;
import static com.marketplace.logger.aspect.utils.ColorUtils.coloredString;
import static com.marketplace.logger.aspect.utils.RequestLogUtils.*;
import static com.marketplace.logger.aspect.utils.ResponseLogUtils.tryToLogResponse;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LoggerAop {

    private final ObjectMapper mapper;
    private final Map<Class<?>, HttpMethod> restMappingMap;

    @Pointcut("within(com.marketplace..*) && @within(org.springframework.web.bind.annotation.RestController) && execution(* *(..))")
    public void logSuccessfulPointCut(){}

    @Pointcut("within(com.marketplace..*) && @within(org.springframework.web.bind.annotation.RestControllerAdvice)")
    public void logControllerErrorBodyPointCut(){}

    @Around("logSuccessfulPointCut()")
    public Object logRequestBody(ProceedingJoinPoint joinPoint) throws Throwable {
        LoggedRequest loggedRequest = new LoggedRequest();
        Method method = retrieveCalledMethod(joinPoint);
        try {
            String requestColor = ANSI_GREEN;
            loggedRequest = new LoggedRequest();
            collectHttpMethodAndPath(loggedRequest, method, restMappingMap);
            collectRequestArguments(joinPoint, loggedRequest, method);
            String stringifyRequest = coloredString(mapper.writeValueAsString(loggedRequest), requestColor);
            log.info(requestColor + String.format(REQUEST, stringifyRequest) + ANSI_RESET);
        }
        catch (Exception e) {
            log.warn(String.format(ANSI_YELLOW + FAILED_TO_LOG_REQUEST, loggedRequest.getPath()));
        }

        LoggedResponse<Object> loggedResponse = new LoggedResponse<>();
        loggedResponse.setPath(loggedRequest.getPath());
        try {
            Object returnedObject = joinPoint.proceed();
            return tryToLogResponse(method, ANSI_GREEN, mapper, returnedObject, loggedResponse);
        }
        catch (Throwable e) {
            log.error(String.format(ANSI_RED + ENDPOINT_EXCEPTION_OCCURRED, loggedRequest.getPath() + ANSI_RESET));
            throw e;
        }
    }

    @AfterReturning(pointcut = "logControllerErrorBodyPointCut()", returning = "returningObject")
    public Object logErrorBody(Object returningObject) {
        LoggedResponse<Object> loggedResponse = new LoggedResponse<>();
        loggedResponse.setHttpCode(ERROR_CODE);
        return tryToLogResponse(null, ANSI_RED, mapper, returningObject, loggedResponse);
    }
}
