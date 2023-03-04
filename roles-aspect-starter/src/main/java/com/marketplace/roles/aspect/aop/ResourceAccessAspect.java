package com.marketplace.roles.aspect.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.roles.aspect.annotations.Owner;
import com.marketplace.roles.aspect.annotations.RolesRequired;
import com.marketplace.roles.common.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.marketplace.roles.utils.HelpAspectMethods.*;
import static com.marketplace.roles.utils.OwnerAspectUtils.*;
import static com.marketplace.roles.utils.RolesAspectUtils.getRequiredRoles;
import static com.marketplace.roles.utils.RolesAspectUtils.rolesProvided;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceAccessAspect implements Constants {

    private final ObjectMapper mapper;

    @Pointcut("@annotation(com.marketplace.roles.aspect.annotations.Owner) || @annotation(com.marketplace.roles.aspect.annotations.RolesRequired)")
    public void verifyAccessToResourcePointCut() {
    }

    @Before("verifyAccessToResourcePointCut()")
    public void verifyResourceAccess(JoinPoint joinPoint) {
        Method method = retrieveCalledMethod(joinPoint);
        boolean rolesOk = false;

        if (method.isAnnotationPresent(RolesRequired.class)) {
            verifyRolesHeader(method, ROLES);
            rolesOk = rolesProvided(joinPoint.getArgs(), getRequiredRoles(method));
        }

        if (method.isAnnotationPresent(Owner.class) && !rolesOk) {
            verifyProfileIdHeader(method, PROFILE_ID);
            Object[] args = joinPoint.getArgs();
            String profileHeaderValue = getProfileHeaderValue(args[0]);
            Map<Boolean, List<Object>> separatedArgs = retrieveSeparatedArgs(Arrays.copyOfRange(args, 1, args.length));
            List<Object> stringArgs = separatedArgs.get(true);

            if (separatedArgs.size() == 0) throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(OWNER_NOT_FOUND.concat(ROLES_HEADER_MUST_BE_SPECIFIED));

            if (stringArgs != null && stringArgs.stream().anyMatch(arg-> arg.equals(profileHeaderValue))) {
                log.info(String.format(OWNER_IS_VALID, profileHeaderValue));
                return;
            }
            verifyOwnerIdInRequestBody(separatedArgs.get(false), profileHeaderValue, mapper);
        }
        else {
            if (!rolesOk) throw new CustomException(HttpStatus.FORBIDDEN).setDetails(PROVIDED_ROLES_NOT_FOUND);
        }
    }
}
