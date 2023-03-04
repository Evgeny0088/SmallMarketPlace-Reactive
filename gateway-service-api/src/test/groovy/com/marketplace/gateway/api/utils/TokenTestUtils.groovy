package com.marketplace.gateway.api.utils

import com.marketplace.ProfileDto
import com.marketplace.gateway.api.TestConstants
import com.marketplace.gateway.api.common.Constants
import com.marketplace.jwt.jwtservice.JWTService
import com.marketplace.jwt.properties.JwtProperties

class TokenTestUtils implements TestConstants, Constants {

    private static final Map<String, String> invalidTokensMap = Map.ofEntries(
            Map.entry(CORRECT, CORRECT),
            Map.entry(WRONG_SIGN, WRONG_SIGN_TOKEN),
            Map.entry(WRONG_ALG, WRONG_ALG_TOKEN),
            Map.entry(EMPTY_T, EMPTY_TOKEN),
            Map.entry(BLANK_T, BLANK_TOKEN),
            Map.entry(BAD_FORMATTING, BAD_FORMATTING_TOKEN)
    )

    static void setupExpiredJwtProperties(JwtProperties jwtProperties, String tokenCondition) {
        if (tokenCondition == EXPIRED) jwtProperties.setTokenExpiresAt(0)
    }

    static void setupWrongSecretJwtProperties(JwtProperties jwtProperties, String tokenCondition) {
        if (tokenCondition == INVALID_SECRET) jwtProperties.setSecret(UUID.randomUUID().toString())
    }

    static String createTestToken(String tokenCondition, JWTService jwtService) {
        ProfileDto testProfile = ProfileDto.builder()
                .id("test")
                .username("test")
                .roles("USER ADMIN")
                .build()
        return (tokenCondition == CORRECT || tokenCondition == INVALID_SECRET || tokenCondition == EXPIRED)
                ? jwtService.createToken(testProfile).getAccessToken()
                : getInvalidToken(tokenCondition)
    }

    static String getInvalidToken(String tokenCondition){
        return invalidTokensMap.get(tokenCondition)
    }
}
