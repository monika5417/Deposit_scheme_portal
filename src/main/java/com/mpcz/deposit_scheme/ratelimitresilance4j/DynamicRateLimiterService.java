package com.mpcz.deposit_scheme.ratelimitresilance4j;


import org.springframework.stereotype.Service;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import java.time.Duration;

@Service
public class DynamicRateLimiterService {
    
    private final RateLimiterRegistry rateLimiterRegistry;
    
    public DynamicRateLimiterService(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }
    
    // Default API validation (5 requests per minute)
    public void validateRequest(String key) {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("API_5_" + key, config);
        
        if (!rateLimiter.acquirePermission()) {
            throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
        }
    }
    
    // Strict validation for specific API (1 request per minute)
    public void validateStrictRequest(String key) {
        RateLimiterConfig strictConfig = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        
        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("API_STRICT_" + key, strictConfig);
        
        if (!rateLimiter.acquirePermission()) {
            throw RequestNotPermitted.createRequestNotPermitted(rateLimiter);
        }
    }
}