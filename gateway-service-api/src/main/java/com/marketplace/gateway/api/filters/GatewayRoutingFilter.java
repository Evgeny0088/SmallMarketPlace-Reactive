package com.marketplace.gateway.api.filters;

import com.marketplace.gateway.api.common.Constants;
import com.marketplace.gateway.api.properties.RouterBaseAuthProperties;
import com.marketplace.gateway.api.properties.RoutesProperties;
import com.marketplace.gateway.api.service.ServiceAggregator;
import com.marketplace.jwt.jwtservice.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.marketplace.gateway.api.filters.FilterMethods.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class GatewayRoutingFilter implements Constants {

    private final RoutesProperties routerProperties;
    private final RouterBaseAuthProperties routerBaseAuthProperties;
    private final ServiceAggregator serviceAggregator;
    private final JWTService jwtService;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(
                ROUTE_ID,
                r -> r.path(ROUTE_PATTERN)
                    .filters(f ->
                        f
                            .rewritePath(routerProperties.getPathPattern(), routerProperties.getReplacementPathTemplate())
                            .filter(((exchange, chain) -> verifyAllowedPathListsFilter(exchange, chain, serviceAggregator)))
                            .filter((exchange, chain) -> rewritePathFilter(exchange, chain, routerProperties, serviceAggregator))
                            .filter((exchange, chain) -> verifyHeadersFilter(exchange, chain, routerBaseAuthProperties))
                            .filter(((exchange, chain) -> verifyJWTFilter(exchange, chain, jwtService)))
                    )
                    .uri(routerProperties.getServerUrl())
            )
            .build();
    }
}
