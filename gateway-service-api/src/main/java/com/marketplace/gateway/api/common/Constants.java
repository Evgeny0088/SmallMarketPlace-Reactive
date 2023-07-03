package com.marketplace.gateway.api.common;

import java.util.regex.Pattern;

public interface Constants {

    String ROUTE_ID = "gateway_route_id";
    String ROUTE_PATTERN = "/**";
    String DOUBLE_SLASH = "//";
    String SLASH = "/";
    String PARAM_MARK = "?";
    String API_URL_PATTERN = "%s:%s%s%s:%s%s";
    String AUTHENTICATION_IS_REQUIRED_PROPERTY = "authenticationIsRequired";
    String AUTH_NOT_REQUIRED_URL = "authNotRequiredUrl";
    String ORIGINAL_URI = "originalUrl";
    String GATEWAY_REQUEST_STATUS = "Incoming request: %s is routed to uri: %s";
    String PATH_PATTERN_REGEX = "^/(?<service>[^/]*)(?<path>/.*)$";
    Pattern PATH_PATTERN = Pattern.compile(PATH_PATTERN_REGEX);

    // authentication
    String UNEXPECTED_ERROR = "Unexpected error is occurred during authorization, see reason below.";
    String HEADERS_ERROR_MESSAGE = "Please check if headers are typed correctly.";
    String TOKEN_NOT_PROVIDED = "Token is required!";

    String HEADER_ACCESS_TOKEN = "Access-Token";
    String HEADER_PROFILE_ID = "Profile-Id";
    String ROLES = "Roles";
    String MARKETPLACE_HEADER_NAME = "Marketplace-Name";
    String MARKETPLACE_HEADER_PASS = "Marketplace-Pass";

    // Error custom fields
    String TITLE = "title";
    String CODE = "code";
    String DETAIL = "detail";
}
