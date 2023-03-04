package com.marketplace.jwt.common;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import java.util.Map;

public interface JwtConstants {

    String EMPTY_TOKEN = "Token is required!";
    String ACCESS_TOKEN = "access-token";
    String FAILED_CREATE_TOKEN = "Token cannot be created, please see error details:\n%s";
    String UNEXPECTED_ERROR = "Unexpected error is occurred during token validation.";

    String SECRET_DELIMITER = "__";
    String PROFILE_ID = "Profile-Id";
    String ROLES = "Roles";

    Map<Class<?>, String> TOKEN_VERIFICATION_ERROR_MAP = Map.ofEntries(
            Map.entry(TokenExpiredException.class, "Token is expired!"),
            Map.entry(AlgorithmMismatchException.class, "Algorithm is not valid for this token!"),
            Map.entry(SignatureVerificationException.class, "Token signature is not valid!"),
            Map.entry(InvalidClaimException.class, "Token content is not valid!")
    );
}
