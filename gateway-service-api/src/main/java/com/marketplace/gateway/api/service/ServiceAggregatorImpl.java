package com.marketplace.gateway.api.service;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.gateway.api.properties.ServiceRoadMap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.marketplace.gateway.api.common.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
public class ServiceAggregatorImpl implements ServiceAggregator, InitializingBean, Constants {

    private ServiceRoadMap serviceRoadMap;
    private Map<String, ServiceInfo> gatewayServices = new HashMap<>();

    public ServiceAggregatorImpl(ServiceRoadMap serviceRoadMap) {
        this.serviceRoadMap = serviceRoadMap;
    }

    @Override
    public void verifyBlackListWordInUrl(String originalPath) {
        if (serviceRoadMap.getBlackListWords().stream().anyMatch(originalPath::contains)){
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean verifyAuthNotRequiredUrlList(String originalPath) {
        return serviceRoadMap.getAuthNotRequiredUrlList().stream().anyMatch((url)-> {
            Pattern pattern = Pattern.compile(url);
            return pattern.matcher(originalPath).find();
        });
    }

    @Override
    public ServiceInfo retrieveServiceInfo(String serviceAlias) {
        ServiceInfo serviceInfo = gatewayServices.get(serviceAlias);
        if (serviceInfo == null){
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return serviceInfo;
    }

    @Override
    public void afterPropertiesSet() {
        gatewayServices = serviceRoadMap.getServices().stream()
            .map(service-> Map.entry(service.getAlias(), service))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}