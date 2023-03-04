package com.marketplace.gateway.api


import com.marketplace.jwt.jwtservice.JWTService
import com.marketplace.jwt.properties.JwtProperties
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST=localhost:4567"],
        classes = [Application.class])
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification implements TestConstants {

    @Autowired
    JWTService jwtService

    @Autowired
    JwtProperties jwtProperties

    @LocalServerPort
    int port

}
