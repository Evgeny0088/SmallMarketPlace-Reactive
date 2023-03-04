package com.marketplace.roles.utils;

import com.marketplace.roles.aspect.annotations.RolesRequired;
import com.marketplace.roles.common.Constants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RolesAspectUtils implements Constants {

    public static Set<String> getRequiredRoles(Method method) {
        try {
            return Arrays.stream(method.getAnnotation(RolesRequired.class).roles()).collect(Collectors.toSet());
        }
        catch (Exception e) {
            log.warn(String.format(FAILED_RETRIEVE_REQUIRED_ROLES));
            return Collections.emptySet();
        }
    }

    public static boolean rolesProvided(Object[] args, Set<String> requiredRoles) {
        for (Object arg: args) {
            try {
                Set<String> providedRoles = Arrays.stream(arg.toString().split(" ")).collect(Collectors.toSet());
                if (requiredRoles.size() >= providedRoles.size()) {
                    if (findAnyMatchByRequiredRoles(requiredRoles, providedRoles)) return true;
                }
                else {
                    if (findAnyMatchByProvidedRoles(requiredRoles, providedRoles)) return true;
                }
            }
            catch (Exception e) {
                log.warn(String.format(FAILED_PARSE_ARGUMENT, arg));
            }
        }
        return false;
    }

    private static boolean findAnyMatchByRequiredRoles (Collection<String> requiredRoles, Collection<String> providedRoles) {
        if (requiredRoles.stream().anyMatch((required)-> providedRoles.stream().anyMatch((provided)-> provided.equals(required)))) {
            log.info(String.format(PROVIDED_ROLES_OK, providedRoles));
            return true;
        }
        return false;
    }

    private static boolean findAnyMatchByProvidedRoles (Collection<String> requiredRoles, Collection<String> providedRoles) {
        if (providedRoles.stream().anyMatch((provided)-> requiredRoles.stream().anyMatch((required)-> required.equals(provided)))) {
            log.info(String.format(PROVIDED_ROLES_OK, providedRoles));
            return true;
        }
        return false;
    }
}