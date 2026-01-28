package com.mpcz.deposit_scheme.ratelimitresilance4j;

import org.springframework.stereotype.Service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;


@Service
public class DynamicRateLimiterService {

    private final RateLimiterRegistry rateLimiterRegistry;

    public DynamicRateLimiterService(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    public void validateRequest(String key) {
        RateLimiter rateLimiter =
                rateLimiterRegistry.rateLimiter("API_LIMIT_" + key);

        if (!rateLimiter.acquirePermission()) {
            throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
        }
    }
}
