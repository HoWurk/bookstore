package com.securemicroservices.service.impl;

import com.securemicroservices.dto.AuthenticationResponse;
import com.securemicroservices.dto.LoginRequest;
import com.securemicroservices.dto.RegistrationRequest;
import com.securemicroservices.dto.UserDTO;
import com.securemicroservices.entity.Role;
import com.securemicroservices.entity.User;
import com.securemicroservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
}
