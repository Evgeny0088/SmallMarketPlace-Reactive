package com.marketplace.gateway.api.service;

import lombok.Data;

@Data
public class ServiceInfo {

    private String alias;
    private String name;
    private int port;
    private boolean authRequired;

}

