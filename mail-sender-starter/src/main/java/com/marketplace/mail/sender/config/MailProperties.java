package com.marketplace.mail.sender.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mail-sender")
public class MailProperties {

    private String host;
    private int port;
    private String protocol;
    private String auth;
    private String sslEnable;
    private String username;
    private String password;
    private String smtpDebug;

}
