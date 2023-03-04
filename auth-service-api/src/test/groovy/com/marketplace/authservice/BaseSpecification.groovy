package com.marketplace.authservice

import com.marketplace.test.helper.starter.specification.AbstractSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST = localhost:4567"],
        classes = [Application.class]
)
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification {

}
