package com.marketplace.gateway.api.util;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.URI;

public class FilterUtilMethods {

    public static Route modifyRoute(Route incomingRoute, URI newUri){
        return Route.async()
            .id(incomingRoute.getId())
            .uri(newUri)
            .order(incomingRoute.getOrder())
            .asyncPredicate(incomingRoute.getPredicate())
            .filters(incomingRoute.getFilters())
            .build();
    }

    public static ServerHttpRequest mutateRequestPath(ServerHttpRequest request, URI newUri) {
        return request.mutate()
                .path(newUri.getPath())
                .build();
    }
}