package com.marketplace.roles.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.roles.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class OwnerAspectUtils implements Constants {

    public static Map<Boolean, List<Object>> retrieveSeparatedArgs(Object[] args){
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(arg-> isUUIDString(arg.toString())));
    }

    public static void verifyOwnerIdInRequestBody(List<Object> args, String ownerIdCandidate, ObjectMapper mapper) {
        boolean ownerIdFound = args.stream().anyMatch((arg)-> {
            try {
                return recursiveRetrieveOwnerId(0, ownerIdCandidate, arg, mapper);
            }
            catch (Exception e){
                log.warn(String.format(CONVERSION_FAILING, arg));
            }
            return false;
        });
        if (!ownerIdFound) {
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(OWNER_NOT_FOUND);
        }
    }

    private static boolean recursiveRetrieveOwnerId(int recursionDepth, String ownerIdCandidate, Object obj, ObjectMapper mapper) {
        if (recursionDepth > RECURSION_DEPTH) {
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(OWNER_NOT_FOUND);
        }
        
        LinkedHashMap<String, Object> objMap = mapper.convertValue(obj, new TypeReference<>() {});

        for (Object value: objMap.values()) {
            if (value.equals(ownerIdCandidate)) return true;
            if (!NON_MAPPABLE_CLASSES.contains(value.getClass())) {
                return recursiveRetrieveOwnerId(++recursionDepth, ownerIdCandidate, value, mapper);
            }
        }
        return false;
    }

    public static String getProfileHeaderValue(Object ownerIdCandidate) {
        if (ownerIdCandidate == null || ownerIdCandidate.toString().isBlank()){
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(ROLES_HEADER_MUST_BE_SPECIFIED);
        }
        String profileHeaderValue = ownerIdCandidate.toString();
        if (!isUUIDString(profileHeaderValue)) {
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(OWNER_BAD_FORMAT, profileHeaderValue));
        }
        return profileHeaderValue;
    }

    public static boolean isUUIDString(String uuidCandidate){
        Pattern pattern = Pattern.compile(UUID_REGEX);
        Matcher matcher = pattern.matcher(uuidCandidate);
        return matcher.find();
    }
}
