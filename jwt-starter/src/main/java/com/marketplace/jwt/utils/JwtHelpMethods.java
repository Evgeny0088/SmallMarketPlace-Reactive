package com.marketplace.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplace.ProfileDto;
import com.marketplace.jwt.common.JwtConstants;
import com.marketplace.jwt.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtHelpMethods implements JwtConstants {

    public static String createToken(ProfileDto profileDto, Algorithm alg, JwtProperties jwtProperties) {
        String withSecret = SECRET_DELIMITER.concat(jwtProperties.getSecret());
        return JWT.create()
                .withSubject(profileDto.getUsername().concat(withSecret))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getTokenExpiresAt()))
                .withIssuer(jwtProperties.getIssuer())
                .withClaim(ROLES, profileDto.getRoles())
                .withClaim(PROFILE_ID, profileDto.getId())
                .sign(alg);
    }

    public static DecodedJWT verifyToken(String accessToken, String secret, Algorithm alg) {
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String[] identities = decodedJWT.getSubject().split(SECRET_DELIMITER);
        if (!identities[1].equals(secret)) {
            throw new InvalidClaimException(UNEXPECTED_ERROR);
        }
        return decodedJWT;
    }
}

