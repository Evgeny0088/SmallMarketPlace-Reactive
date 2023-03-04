package com.marketplace.profile

import com.marketplace.mail.sender.service.MailService
import com.marketplace.profile.common.Constants
import com.marketplace.profile.common.TestConstants
import com.marketplace.profile.mapper.ProfileMapper
import com.marketplace.profile.properties.ProfileUpdateProperties
import com.marketplace.profile.repository.ProfileRepo
import com.marketplace.test.helper.starter.specification.AbstractSpecification
import com.marketplace.test.helper.starter.testContainers.PostgresContainerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ["SERVICE_HOST = localhost:4567"],
        classes = [Application.class]
)
@ActiveProfiles("test")
class BaseSpecification extends AbstractSpecification implements Constants, TestConstants {

    @Autowired
    MailService mailService

    @Autowired
    ProfileRepo profileRepo

    @Autowired
    ProfileMapper profileMapper

    @Autowired
    ProfileUpdateProperties profileProperties

    @Container
    static PostgreSQLContainer postgreSQLContainer

    def setupSpec(){
        postgreSQLContainer = PostgresContainerFactory.getPostgresContainer()
    }

}
