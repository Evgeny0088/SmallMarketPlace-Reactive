package com.marketplace.error.handler.common;

public interface Constants {

    String ERROR_LOG = "Exception is occurred, please see details below:\n%s";
    String ERROR_FROM_R2DBC_DUPLICATE_KEY = "DataIntegrityViolationException";
    String PROFILE_ALREADY_EXISTS = "Profile already exists!";
    String EMPTY = "";
    String FIELD_ERROR_DELIMITER = " - ";
    String ERROR_PAIR_DELIMITER = " :: ";
}
