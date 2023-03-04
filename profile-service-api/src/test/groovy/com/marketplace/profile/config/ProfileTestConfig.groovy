package com.marketplace.profile.config

import com.marketplace.mail.sender.service.MailService
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class ProfileTestConfig {

    @Bean
    @Primary
    MailService mailService() {
        return Mockito.mock(MailService.class)
    }

}
