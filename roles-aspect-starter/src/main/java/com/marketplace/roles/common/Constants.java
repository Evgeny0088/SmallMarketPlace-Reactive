package com.marketplace.roles.common;

import java.util.List;

public interface Constants {

    String PROFILE_ID = "Profile-Id";
    String ROLES = "Roles";

    String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    // exception messages
    String PROFILE_HEADER_MUST_BE_SPECIFIED = "Header { %s } must be placed at first place and not empty in controller method!";
    String ROLES_HEADER_MUST_BE_SPECIFIED = "Header { %s } must be specified in request!";
    String OWNER_IS_VALID = "Owner id { %s } is valid, request have been passed through.";
    String OWNER_BAD_FORMAT = "Owner id must be uuid string, instead of { %s }";
    String OWNER_NOT_FOUND = "Request is implemented not by owner.";
    String CONVERSION_FAILING = "Conversion type failed on field { %s }";
    String FAILED_RETRIEVE_REQUIRED_ROLES = "Cannot retrieve required roles, default role ('USER') will be used instead.";
    String PROVIDED_ROLES_OK = "Provided roles { '%s' } are OK, request have been passed through.";
    String PROVIDED_ROLES_NOT_FOUND = "Required roles for this resource aren,t provided, request have been refused!";
    String FAILED_PARSE_ARGUMENT = "Failed to parse argument ( %s ), moving to the next one.";
    int RECURSION_DEPTH = 5;

    List<Class<?>> NON_MAPPABLE_CLASSES = List.of(
            String.class, Number.class, Boolean.class
    );
}
