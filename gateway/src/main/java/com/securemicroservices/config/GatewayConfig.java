package com.securemicroservices.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;

import static java.time.Duration.ofSeconds;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtFilter filter;
//    private final JwtUtil jwtUtil;
//    private static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("app_route", r -> r.path("/books/**")
                        .filters(f -> f.filter(filter)
                                .requestRateLimiter(rateLimiter -> rateLimiter
                                        .setRateLimiter(rateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("http://app:8080"))
                .route("auth_route", r -> r.path("/auth/**").or().path("/users/**").and().not(p -> p.path("/auth/logout"))
                        .filters(f -> f.filter(filter)
                                .modifyRequestBody(String.class, String.class, GatewayConfig::extractIpAddress)
                                .requestRateLimiter(rateLimiter -> rateLimiter
                                        .setRateLimiter(rateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("http://auth-service:8080"))
                .route("order_route", r -> r.path("/orders/**").or().path("/order-items/**")
                        .filters(f -> f.filter(filter)
                                .requestRateLimiter(rateLimiter -> rateLimiter
                                        .setRateLimiter(rateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("http://order-service:8080"))
                .route("payment_route", r -> r.path("/payments/**")
                        .filters(f -> f.filter(filter)
                                .requestRateLimiter(rateLimiter -> rateLimiter
                                        .setRateLimiter(rateLimiter())
                                        .setKeyResolver(keyResolver())))
                        .uri("http://payment-service:8080"))
                .build();

    }

    private static Mono<String> extractIpAddress(ServerWebExchange exchange, String body) {
        String ipAddress = exchange.getRequest()
                .getHeaders().getFirst("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            InetSocketAddress remoteAddress = exchange.getRequest()
                    .getRemoteAddress();
            if (remoteAddress != null) {
                ipAddress = remoteAddress.getAddress().getHostAddress();
            }
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.addAll(exchange.getRequest().getHeaders());
        headers.set("Client-IP", ipAddress);

        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .build();

        exchange.mutate().request(request).build();

        System.out.println(exchange.getRequest().getHeaders());
        return Mono.just(body);
    }

    @Bean
    public RateLimiter<BucketRateLimiter.RateLimiterConfig> rateLimiter() {
        return new BucketRateLimiter(1, 5, ofSeconds(10));
    }

    @Bean
    public KeyResolver keyResolver() {
        return exchange -> {
            ServerHttpRequest request = exchange.getRequest();

            String ipAddress = request.getHeaders().getFirst("X-Forwarded-For");

            if (ipAddress == null || ipAddress.isEmpty()) {
                InetSocketAddress remoteAddress = request.getRemoteAddress();

                if (remoteAddress != null) {
                    ipAddress = remoteAddress.getAddress().getHostAddress();
                }
            } else {
                ipAddress = ipAddress.split(",")[0].trim();
            }

            return Mono.just(Objects.requireNonNull(ipAddress));
        };
    }
}
