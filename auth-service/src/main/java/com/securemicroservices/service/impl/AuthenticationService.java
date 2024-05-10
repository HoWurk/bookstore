package com.securemicroservices.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.securemicroservices.dto.AuthenticationResponse;
import com.securemicroservices.dto.LoginRequest;
import com.securemicroservices.dto.RegistrationRequest;
import com.securemicroservices.dto.UserDTO;
import com.securemicroservices.entity.Role;
import com.securemicroservices.entity.User;
import com.securemicroservices.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final HttpServletRequest request;

    private final LoadingCache<String, Integer> attemptsCache;
    private static final int MAX_ATTEMPT = 5;

    @Autowired
    public AuthenticationService(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, HttpServletRequest request) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.request = request;
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public AuthenticationResponse register(RegistrationRequest request) {
        UserDTO user = UserDTO.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User userCreated = userService.createUser(user);

        String token = jwtService.generateToken(userCreated);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(LoginRequest request) {
        if (isBlocked()) {
            throw new RuntimeException("Blocked");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked() {
        try {
            return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    private String getClientIP() {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteHost();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }
//        String ipAddress = request.getHeader("Client-IP");
        return ipAddress;
    }
}
