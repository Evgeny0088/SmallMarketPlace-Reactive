
package com.marketplace.jwt.jwtservice;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplace.ProfileDto;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.jwt.common.JwtConstants;
import com.marketplace.jwt.dto.JwtDto;
import com.marketplace.jwt.properties.JwtProperties;
import com.marketplace.jwt.utils.JwtHelpMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JWTServiceImpl implements JWTService, JwtConstants {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;

    public JWTServiceImpl(JwtProperties jwtProperties,
                          @Qualifier("jwt-algorithm") Algorithm algorithm) {
        this.jwtProperties = jwtProperties;
        this.algorithm = algorithm;
    }

    @Override
    public JwtDto createToken (ProfileDto profileDto) {
        try {
            return JwtDto.builder()
                    .accessToken(JwtHelpMethods.createToken(profileDto, algorithm, jwtProperties))
                    .build();
        }
        catch (Exception exception){
            throw new CustomException(HttpStatus.UNAUTHORIZED)
                    .setDetails(String.format(FAILED_CREATE_TOKEN, exception.getMessage()));
        }
    }

    @Override
    public DecodedJWT verifyToken (String token) {
        if (token != null) {
            try {
                return JwtHelpMethods.verifyToken(token, jwtProperties.getSecret(), algorithm);
            }
            catch (Exception exception) {
                throw new CustomException(HttpStatus.UNAUTHORIZED)
                        .setDetails(TOKEN_VERIFICATION_ERROR_MAP.getOrDefault(exception.getClass(), UNEXPECTED_ERROR));
            }
        }
        else {
            throw new CustomException(HttpStatus.UNAUTHORIZED)
                    .setDetails(EMPTY_TOKEN);
        }
    }
}

