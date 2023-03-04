package com.marketplace.profile.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "profile-updates")
public class ProfileUpdateProperties {

    private long updateAfter;
    private long expiredEmailAfter;
    private long requestEmailAfter;
    private PageableProperties pageableProperties;
}
