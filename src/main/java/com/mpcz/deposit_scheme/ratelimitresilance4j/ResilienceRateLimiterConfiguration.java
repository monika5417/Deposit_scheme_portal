package com.mpcz.deposit_scheme.ratelimitresilance4j;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;


@Configuration
public class ResilienceRateLimiterConfiguration {

	@Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        // Default config (fallback)
        RateLimiterConfig defaultConfig = RateLimiterConfig.custom()
                .limitForPeriod(5)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .timeoutDuration(Duration.ZERO)
                .build();
        
        return RateLimiterRegistry.of(defaultConfig);
    }
}
