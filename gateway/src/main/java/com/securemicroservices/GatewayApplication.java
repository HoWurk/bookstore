package com.securemicroservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("app_route", r -> r.path("/books/**")
                        .uri("http://app:8080"))
                .route("auth_route", r -> r.path("/auth/**", "/users/**")
                        .uri("http://auth-service:8080"))
                .route("order_route", r -> r.path("/orders/**").or().path("/order-items/**")
                        .uri("http://order-service:8080"))
                .route("payment_route", r -> r.path("/payments/**")
                        .uri("http://payment-service:8080"))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}