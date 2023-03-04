package com.marketplace.logger.aspect.common;

public interface Constants {

    String EMPTY = "";
    String REQUEST = "Request is logged:\n%s";
    String RESPONSE = "Response is logged:\n%s";
    String UNKNOWN_HTTP_METHOD = "HttpMethod is not defined";
    String ERROR_CODE = "Error code.";
    String UNKNOWN_ARGUMENT = "Unknown argument";
    String FAILED_TO_LOG_REQUEST = "Failed to log request for endpoint { %s }.";
    String FAILED_TO_LOG_RESPONSE = "Failed to log response body for endpoint { %s }";
    String PATH_REGEX = "^\\/.+$"; // for any /* patterns
    String PATH_VARIABLE_REGEX = "(\\{.*?\\})";
    String FIRST_REQUEST_PARAM = "?%s=%s";
    String NEXT_REQUEST_PARAM = "&%s=%s";
    String NEW_LINE_DELIMITER = "\n";
    String ENDPOINT_EXCEPTION_OCCURRED = "Exception is occurred on call { %s } endpoint.";
    String FAILED_TO_GET_PATH_AND_METHOD = "Failed to retrieve path and method details -> default values will be used for logging.";
    String REACTIVE_BODY_CONTENT = "Reactive body >>> %s";

    String PATH_REGEX_BACKUP = "^\\/.+[^\\/]+\\/[^\\/]+$"; // for /{<segment>}/* patterns
}
