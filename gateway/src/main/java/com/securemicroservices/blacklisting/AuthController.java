package com.securemicroservices.blacklisting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/logout")
@RequiredArgsConstructor
public class AuthController {
    private final JwtTokenService jwtTokenService;

    @DeleteMapping
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization") String token) {
        try {
            jwtTokenService.logout(token);
            return ResponseEntity.ok("Logged out successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid token: " + e.getMessage());
        }
    }
}

