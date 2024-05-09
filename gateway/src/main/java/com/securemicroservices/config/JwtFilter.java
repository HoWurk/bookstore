package com.securemicroservices.config;


import com.securemicroservices.blacklisting.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@RefreshScope
@Component
@RequiredArgsConstructor
public class JwtFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final JwtUtil jwtUtil;
    private final JwtTokenService jwtTokenService;

    @Value("${secret.shared}")
    private String sharedSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders httpHeaders = request.getHeaders();
        String authorizationHeader = httpHeaders.getFirst(HttpHeaders.AUTHORIZATION);
        String sharedSecretHeader = httpHeaders.getFirst("X-SHARED-SECRET");

        if (sharedSecretHeader != null) {
            if (!sharedSecretHeader.equals(sharedSecret)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            } else {
                return chain.filter(exchange);
            }
        }

        if (routerValidator.isSecured.test(request)) {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                if (jwtUtil.isInvalid(jwt) || jwtTokenService.isTokenBlacklisted(jwt)) {
                    return onError(exchange, HttpStatus.FORBIDDEN, "JWT token is expired or invalid.");
                }
                Set<String> roles = jwtUtil.extractRoles(jwt);
                return filterRoutes(roles, request, exchange, chain);
            } else {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }
        return chain.filter(exchange);
    }

    // Leaving space for future options (other paths requiring different roles)
    private Mono<Void> filterRoutes(Set<String> userRoles, ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = request.getPath().toString();
        HttpMethod method = request.getMethod();

        // auth-service
        if (path.startsWith("/users")) {
            if (!userRoles.contains("ROLE_ADMIN")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }

        // app-service
        if (path.startsWith("/books")) {
            if (!method.equals(HttpMethod.GET) && !userRoles.contains("ROLE_ADMIN")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }

        // order-service
        if (path.startsWith("/orders") || path.startsWith("/order-items")) {
            if (!userRoles.contains("ROLE_ADMIN")) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }

        // payment-service
        if (path.startsWith("/payments")) {
            if (method.equals(HttpMethod.DELETE) ||
                    (path.contains("/{id}") && method.equals(HttpMethod.GET))) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMessage) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "text/plain");
        System.out.println("Access denied: " + httpStatus.getReasonPhrase() + " - " + errorMessage);
        byte[] errorMessageBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(errorMessageBytes);
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> DataBufferUtils.release(buffer));
    }
}
