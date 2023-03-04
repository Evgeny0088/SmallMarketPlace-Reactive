package com.marketplace.roles.utils;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.roles.common.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HelpAspectMethods implements Constants {

    public static Method retrieveCalledMethod(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public static void verifyProfileIdHeader(Method method, String annotationValue) {
        try {
            Parameter parameter = method.getParameters()[0];
            if (parameter.isAnnotationPresent(RequestHeader.class)) {
                Annotation annotation  = parameter.getAnnotation(RequestHeader.class);
                for (Method annotationMethod: annotation.annotationType().getDeclaredMethods()) {
                    if (annotationMethod.invoke(annotation).equals(annotationValue)) return;
                }
            }
            throw new RuntimeException();
        }
        catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .setDetails(String.format(PROFILE_HEADER_MUST_BE_SPECIFIED, annotationValue));
        }
    }

    public static void verifyRolesHeader(Method method, String annotationValue) {
        try {
            for (Parameter parameter: method.getParameters()){
                if (parameter.isAnnotationPresent(RequestHeader.class)) {
                    Annotation annotation  = parameter.getAnnotation(RequestHeader.class);
                    for (Method annotationMethod: annotation.annotationType().getDeclaredMethods()) {
                        if (annotationMethod.invoke(annotation).equals(annotationValue)) return;
                    }
                }
            }
            throw new RuntimeException();
        }
        catch (Exception e){
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .setDetails(String.format(ROLES_HEADER_MUST_BE_SPECIFIED, annotationValue));
        }
    }
}
