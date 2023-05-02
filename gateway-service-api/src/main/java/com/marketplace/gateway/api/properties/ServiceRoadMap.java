package com.marketplace.gateway.api.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.marketplace.gateway.api.common.Constants;
import com.marketplace.gateway.api.service.ServiceInfo;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("service-road-map")
public class ServiceRoadMap implements Constants {

    /*
    ### ALLOWED URL LIST WITHOUT HEADERS AND TOKEN
    */
    List<String> authNotRequiredUrlList;

    /*
    ### NOT ALLOWED WORDS IN INCOMING URL
     */
    List<String> blackListWords;

    /*
    ### ALLOWED SERVICES GOING THROUGH GATEWAY
     */
    List<ServiceInfo> services;
}
