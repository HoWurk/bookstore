package com.securemicroservices.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("app_route", r -> r.path("/books/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://app:8080"))
                .route("auth_route", r -> r.path("/auth/**").or().path("/users/**").and().not(p -> p.path("/auth/logout"))
                        .filters(f -> f.filter(filter))
                        .uri("http://auth-service:8080"))
                .route("order_route", r -> r.path("/orders/**").or().path("/order-items/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://order-service:8080"))
                .route("payment_route", r -> r.path("/payments/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://payment-service:8080"))
                .build();

    }
}
