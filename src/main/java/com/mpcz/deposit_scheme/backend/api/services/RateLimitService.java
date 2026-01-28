package com.mpcz.deposit_scheme.backend.api.services;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

@Component
public class RateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key, int limitPerMinute) {
        return cache.computeIfAbsent(key, k -> newBucket(limitPerMinute));
    }

    private Bucket newBucket(int limit) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(
                        limit,
                        Refill.greedy(limit, Duration.ofMinutes(1))))
                .build();
    }
}
