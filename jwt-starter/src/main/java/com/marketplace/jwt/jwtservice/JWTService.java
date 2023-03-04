package com.marketplace.jwt.jwtservice;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplace.ProfileDto;
import com.marketplace.jwt.dto.JwtDto;

public interface JWTService {

    JwtDto createToken(ProfileDto profileDto);

    DecodedJWT verifyToken(String token);

}
