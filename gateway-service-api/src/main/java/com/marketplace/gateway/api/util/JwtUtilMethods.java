package com.marketplace.gateway.api.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.gateway.api.common.Constants;
import com.marketplace.jwt.jwtservice.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Slf4j
public class JwtUtilMethods implements Constants {

    public static DecodedJWT validateAccessToken(ServerWebExchange exchange, JWTService jwtService) {
        List<String> tokens = exchange.getRequest().getHeaders().get(Constants.HEADER_ACCESS_TOKEN);
        if (ObjectUtils.isEmpty(tokens) || Strings.isBlank(tokens.get(0))) {
            throw new CustomException(HttpStatus.UNAUTHORIZED).setDetails(TOKEN_NOT_PROVIDED);
        }
        return jwtService.verifyToken(tokens.get(0));
    }

    public static ServerWebExchange addProfileIdAndRolesToExchange(ServerWebExchange exchange, DecodedJWT claimsSet) {
        String profileId = claimsSet.getClaim(HEADER_PROFILE_ID).asString();
        String roles = claimsSet.getClaim(ROLES).asString();
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(HEADER_PROFILE_ID, profileId)
                .header(ROLES, roles)
                .build();
        return exchange.mutate().request(mutatedRequest).build();
    }
}
