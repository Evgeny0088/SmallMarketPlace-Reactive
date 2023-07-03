package com.marketplace.gateway.api.filters;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.gateway.api.common.Constants;
import com.marketplace.gateway.api.properties.RouterBaseAuthProperties;
import com.marketplace.gateway.api.properties.RoutesProperties;
import com.marketplace.gateway.api.service.ServiceAggregator;
import com.marketplace.gateway.api.service.ServiceInfo;
import com.marketplace.jwt.jwtservice.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Objects;

import static com.marketplace.gateway.api.util.FilterUtilMethods.modifyRoute;
import static com.marketplace.gateway.api.util.FilterUtilMethods.mutateRequestPath;
import static com.marketplace.gateway.api.util.JwtUtilMethods.addProfileIdAndRolesToExchange;
import static com.marketplace.gateway.api.util.JwtUtilMethods.validateAccessToken;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

@Slf4j
public class FilterMethods implements Constants {

    public static Mono<Void> verifyAllowedPathListsFilter(ServerWebExchange exchange,
                                                          GatewayFilterChain chain,
                                                          ServiceAggregator serviceAggregator) {
        LinkedHashSet<URI> originalURIs = exchange.getRequiredAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI originalUri = originalURIs.stream().findFirst()
                                                    .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND));
        String originalPath = originalUri.getPath().toLowerCase();

        serviceAggregator.verifyBlackListWordInUrl(originalPath);
        exchange.getAttributes().put(AUTH_NOT_REQUIRED_URL, serviceAggregator.verifyAuthNotRequiredUrlList(originalPath));
        exchange.getAttributes().put(ORIGINAL_URI, originalUri);
        return chain.filter(exchange);
    }

    public static Mono<Void> rewritePathFilter(ServerWebExchange exchange,
                                               GatewayFilterChain chain,
                                               RoutesProperties routerProperties,
                                               ServiceAggregator serviceAggregator){
        try {
            ServerHttpRequest request = exchange.getRequest();
            URI inputUri = request.getURI();
            String inputPath = inputUri.getPath();
            String rewrittenPath = StringUtils.isNotBlank(inputUri.getQuery())
                    ? inputPath.concat(PARAM_MARK + inputUri.getQuery()).toLowerCase()
                    : inputPath.toLowerCase();
            String serviceAlias = StringUtils.substringBetween(Objects.requireNonNull(rewrittenPath), SLASH, SLASH).toLowerCase();
            String targetPath = StringUtils.substringAfter(rewrittenPath, serviceAlias);

            ServiceInfo serviceInfo = serviceAggregator.retrieveServiceInfo(serviceAlias);

            String newUrl = String.format(API_URL_PATTERN,
                    request.getURI().getScheme(),
                    DOUBLE_SLASH,
                    serviceInfo.getName(),
                    routerProperties.getStand(),
                    serviceInfo.getPort(),
                    targetPath)
                    .toLowerCase();

            URI newUri = new URI(newUrl);
            Route modifiedRoute = modifyRoute(Objects.requireNonNull(exchange.getAttribute(GATEWAY_ROUTE_ATTR)), newUri);

            exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, modifiedRoute);
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);
            exchange.getAttributes().put(AUTHENTICATION_IS_REQUIRED_PROPERTY, serviceInfo.isAuthRequired());
            return chain.filter(exchange.mutate().request(mutateRequestPath(request, newUri)).build());
        }
        catch (URISyntaxException e) {
            throw new CustomException(HttpStatus.NOT_FOUND).setDetails(e.getMessage());
        }
    }

    public static Mono<Void> verifyHeadersFilter(ServerWebExchange exchange,
                                                 GatewayFilterChain chain,
                                                 RouterBaseAuthProperties routerBaseAuthProperties) {
        ServerHttpRequest request = exchange.getRequest();
        String marketPlaceName = request.getHeaders().getFirst(MARKETPLACE_HEADER_NAME);
        String marketPlacePass = request.getHeaders().getFirst(MARKETPLACE_HEADER_PASS);
        
        String fromSettingsName = routerBaseAuthProperties.getMarketPlaceName().trim();
        String fromSettingsPass = routerBaseAuthProperties.getMarketPlacePass().trim();
        boolean authIsRequired = (boolean) exchange.getAttributes().get(AUTH_NOT_REQUIRED_URL);
        
        if (!authIsRequired && (!fromSettingsName.equals(marketPlaceName) || !fromSettingsPass.equals(marketPlacePass))) {
            throw new CustomException(HttpStatus.UNAUTHORIZED)
                                  .setDetails(HEADERS_ERROR_MESSAGE);
        }
        return chain.filter(exchange);
    }

    public static Mono<Void> verifyJWTFilter(ServerWebExchange exchange,
                                             GatewayFilterChain chain,
                                             JWTService jwtService){
        URI originalUri = exchange.getAttribute(ORIGINAL_URI);
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        String incomingPath = String.valueOf(originalUri);
        String updatedPath = Objects.requireNonNull(route).getUri().toString();

        if (!(Boolean) exchange.getAttributes().get(AUTH_NOT_REQUIRED_URL) && (Boolean) exchange.getAttributes().get(AUTHENTICATION_IS_REQUIRED_PROPERTY)) {
            return Mono.fromCallable(()->validateAccessToken(exchange, jwtService))
                .doOnNext((logger)-> log.info(String.format(GATEWAY_REQUEST_STATUS, incomingPath, updatedPath)))
                .flatMap((decodedJWT)-> chain.filter(addProfileIdAndRolesToExchange(exchange, decodedJWT)))
                .onErrorMap(Exceptions::propagate)
                .subscribeOn(Schedulers.boundedElastic())
                .log();
        }
        log.info(String.format(GATEWAY_REQUEST_STATUS, incomingPath, updatedPath));
        return chain.filter(exchange);
    }
}

