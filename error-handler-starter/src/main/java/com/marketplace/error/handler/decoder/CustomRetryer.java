package com.marketplace.error.handler.decoder;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/*
Probably this feign retry application would be removed in future in case if circuit breaker will be applied in gateway service
 */
@Component
@ConditionalOnProperty(value = "retryer.enabled", havingValue = "true")
public class CustomRetryer implements Retryer {
    
    private final RetryerProperties props;

    public CustomRetryer(RetryerProperties props) {
        this.props = props;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
            throw e;
    }

    @Override
    public Retryer clone() {
        return new Default(props.getBackoff(), TimeUnit.SECONDS.toMillis(props.getMaxPeriod()), props.getMaxAttempts());
    }
}
