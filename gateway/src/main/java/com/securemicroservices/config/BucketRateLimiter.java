package com.securemicroservices.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.Duration.ofSeconds;

public class BucketRateLimiter extends AbstractRateLimiter<BucketRateLimiter.RateLimiterConfig> {

    private static final String CONFIGURATION_PROPERTY_NAME = "bucket-rate-limiter";
    private static final String REMAINING_HEADER = "X-RateLimit-Remaining";
    private static final String REPLENISH_RATE_HEADER = "X-RateLimit-Replenish-Rate";
    private static final String BURST_CAPACITY_HEADER = "X-RateLimit-Burst-Capacity";

    private final Map<String, Bucket> ipBucketMap = new ConcurrentHashMap<>();

    private final RateLimiterConfig rateLimiterConfig;

    public BucketRateLimiter(int replenishRate, int burstCapacity, @NotNull Duration duration) {
        super(RateLimiterConfig.class, CONFIGURATION_PROPERTY_NAME, null);
        this.rateLimiterConfig = new RateLimiterConfig()
                .setReplenishRate(replenishRate)
                .setBurstCapacity(burstCapacity)
                .setDuration(duration);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        int replenishRate = rateLimiterConfig.getReplenishRate();
        int burstCapacity = rateLimiterConfig.getBurstCapacity();

        Bucket bucket = ipBucketMap.computeIfAbsent(id, k -> {
            var refill = Refill.greedy(replenishRate, rateLimiterConfig.getDuration());
            var limit = Bandwidth.classic(burstCapacity, refill);
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });

        var probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return Mono.just(new RateLimiter.Response(true, headers(probe.getRemainingTokens())));
        } else {
            return Mono.just(new RateLimiter.Response(false, headers(-1)));
        }
    }

    private Map<String, String> headers(long tokensLeft) {
        return Map.of(
                REMAINING_HEADER, Long.toString(tokensLeft),
                REPLENISH_RATE_HEADER, Integer.toString(rateLimiterConfig.getReplenishRate()),
                BURST_CAPACITY_HEADER, Integer.toString(rateLimiterConfig.getReplenishRate())
        );
    }

    @Data
    @Accessors(chain = true)
    public static class RateLimiterConfig {
        private int replenishRate;
        private int burstCapacity = 0;
        private Duration duration = ofSeconds(1);
    }
}
