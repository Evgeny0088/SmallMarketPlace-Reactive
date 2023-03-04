package com.marketplace.logger.aspect.utils;

import com.marketplace.logger.aspect.common.Constants;
import com.marketplace.logger.aspect.dto.LoggedRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.CorePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import static com.marketplace.logger.aspect.common.ColorConstants.ANSI_RESET;
import static com.marketplace.logger.aspect.common.ColorConstants.ANSI_YELLOW;

@Slf4j
public class RequestLogUtils implements Constants {

    public static Method retrieveCalledMethod(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public static void collectHttpMethodAndPath(LoggedRequest loggedRequest, Method method, Map<Class<?>, HttpMethod> restMappingMap) {
        RequestMapping requestMappingAnnotation = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        if (requestMappingAnnotation != null) retrievePathAndMethodFromAnnotation(requestMappingAnnotation, loggedRequest);
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<?> annotationType = annotation.annotationType();
            if (annotationType == RequestMapping.class) {
                retrievePathAndMethodFromAnnotation(annotation, loggedRequest);
                return;
            }
            if (restMappingMap.containsKey(annotationType)) {
                loggedRequest.setHttpMethod(restMappingMap.get(annotationType).name());
                retrievePathAndMethodFromAnnotation(annotation, loggedRequest);
                return;
            }
        }
    }

    public static void collectRequestArguments(ProceedingJoinPoint joinPoint, LoggedRequest loggedRequest, Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] argsValues = joinPoint.getArgs();
        boolean isFirstParam = true;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(PathVariable.class) || parameters[i].isAnnotationPresent(RequestParam.class)) {
                retrievePathVariable(parameters[i], loggedRequest, argsValues[i]);
                isFirstParam = retrieveRequestParam(parameters[i], loggedRequest, argsValues[i], isFirstParam);
            }
            else {
                defineRequestArgument(argsValues[i], loggedRequest, parameters[i].getName(), parameters[i].getType().getSimpleName());
            }
        }
    }

    private static void retrievePathAndMethodFromAnnotation(Annotation annotation, LoggedRequest loggedRequest) {
        Class<?> annotationType = annotation.annotationType();
        for (Method m : annotationType.getDeclaredMethods()) {
            try {
                Object invokedValue = m.invoke(annotation);
                if (!(invokedValue instanceof RequestMethod[]) && !(invokedValue instanceof RequestMethod)) {
                    String path = invokedValue instanceof String[] array && array.length > 0
                            ? array[0]
                            : invokedValue.toString();
                    Pattern p = Pattern.compile(PATH_REGEX);
                    if (p.matcher(path).find()) {
                        loggedRequest.setPath(loggedRequest.getPath().concat(path));
                    }
                }
                else {
                    String method = invokedValue instanceof RequestMethod[]
                            ? Arrays.stream((RequestMethod[]) invokedValue).findFirst().orElse(RequestMethod.GET).name()
                            : ((RequestMethod) invokedValue).name();
                    loggedRequest.setHttpMethod(method);
                }
            }
            catch (Exception e) {
                log.warn(ANSI_YELLOW + FAILED_TO_GET_PATH_AND_METHOD + ANSI_RESET);
            }
        }
    }

    private static void defineRequestArgument(Object arg, LoggedRequest loggedRequest, String parameterName, String typeName) {
        String namedArg = String.format("%s %s", typeName, parameterName);
        try {
            if (arg != null) addArgumentToLogRequest(arg, namedArg, loggedRequest);
        }
        catch (Exception e) {
            loggedRequest.getArguments().putIfAbsent(namedArg, UNKNOWN_ARGUMENT);
        }
    }

    private static void retrievePathVariable(Parameter parameter, LoggedRequest loggedRequest, Object arg){
        if (parameter.isAnnotationPresent(PathVariable.class) && arg != null) {
            loggedRequest.setPath(loggedRequest.getPath().replaceFirst(PATH_VARIABLE_REGEX, arg.toString()));
        }
    }

    private static boolean retrieveRequestParam(Parameter parameter, LoggedRequest loggedRequest, Object arg, boolean isFirstParam){
        if (parameter.isAnnotationPresent(RequestParam.class) && arg != null) {
            loggedRequest.setPath(isFirstParam
                    ? loggedRequest.getPath().concat(String.format(FIRST_REQUEST_PARAM, parameter.getName(), arg))
                    : loggedRequest.getPath().concat(String.format(NEXT_REQUEST_PARAM, parameter.getName(), arg)));
            return false;
        }
        else return isFirstParam;
    }

    private static void addArgumentToLogRequest(Object arg, String namedArg, LoggedRequest loggedRequest) {
        if (arg instanceof CorePublisher<?> publisher) {
            if (publisher instanceof Mono<?> mono) {
                mono.map(obj -> {
                            loggedRequest.getArguments().putIfAbsent(namedArg, obj);
                            return loggedRequest;
                        })
                        .doOnError((e)-> loggedRequest.getArguments().putIfAbsent(namedArg, UNKNOWN_ARGUMENT))
                        .subscribe();
            }
            if (publisher instanceof Flux<?> flux) {
                flux.collectList()
                        .map(obj->{
                            loggedRequest.getArguments().putIfAbsent(namedArg, obj);
                            return loggedRequest;
                        })
                        .doOnError((e)-> loggedRequest.getArguments().putIfAbsent(namedArg, UNKNOWN_ARGUMENT))
                        .subscribe();
            }
        }
        if (arg instanceof ServerHttpRequest request) {
            arg = request.getURI();
        }
        if (arg instanceof InputStreamResource resource){
            arg = resource.getDescription();
        }
        loggedRequest.getArguments().putIfAbsent(namedArg, arg);
    }
}