package com.marketplace.mail.sender.config;

import com.marketplace.mail.sender.common.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailConfig implements Constants {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty(MAIL_SMTP_PROTOCOL, mailProperties.getProtocol());
        properties.setProperty(MAIL_SMTP_AUTH, mailProperties.getAuth());
        properties.setProperty(MAIL_SMTP_SSL, mailProperties.getSslEnable());
        properties.setProperty(MAIL_SMTP_DEBUG, mailProperties.getSmtpDebug());
        return mailSender;
    }
}
