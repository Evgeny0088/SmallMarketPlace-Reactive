package com.marketplace.profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncoderConfig {

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
