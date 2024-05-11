package com.securemicroservices.controller;

import com.securemicroservices.service.impl.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Endpoint(id = "login-attempts")
public class LoginAttemptsEndpoint {

    private final AuthenticationService authenticationService;

    @ReadOperation
    public Map<String, Integer> getLoginAttempts() {
        return authenticationService.getLoginAttempts().asMap();
    }
}

