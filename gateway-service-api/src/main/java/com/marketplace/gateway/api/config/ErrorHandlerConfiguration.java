package com.marketplace.gateway.api.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import com.marketplace.gateway.api.handler.DefaultExceptionHandler;

import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class ErrorHandlerConfiguration {

    @Bean
    @Order(-10)
    public DefaultErrorWebExceptionHandler exceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                                                            ServerProperties serverProperties, ApplicationContext applicationContext,
                                                            ObjectProvider<ViewResolver> viewResolvers,
                                                            ServerCodecConfigurer serverCodecConfigurer) {
        DefaultErrorWebExceptionHandler exceptionHandler = new DefaultExceptionHandler(errorAttributes, webProperties.getResources(), serverProperties.getError(), applicationContext);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
