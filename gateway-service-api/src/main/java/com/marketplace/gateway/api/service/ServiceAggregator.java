package com.marketplace.gateway.api.service;

import com.marketplace.gateway.api.common.Constants;

public interface ServiceAggregator extends Constants {

    void verifyBlackListWordInUrl(String originalPath);

    boolean verifyAuthNotRequiredUrlList(String originalPath);

    ServiceInfo retrieveServiceInfo(String serviceAlias);

}
