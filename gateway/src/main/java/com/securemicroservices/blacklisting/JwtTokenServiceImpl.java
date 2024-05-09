package com.securemicroservices.blacklisting;

import com.securemicroservices.config.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private JwtTokenRepository jwtTokenRepository;
    private JwtUtil jwtUtil;

    @Override
    public void logout(String token) {
        if (!token.startsWith("Bearer ") || jwtUtil.isInvalid(token.substring(7))) {
            throw new IllegalArgumentException("Invalid token");
        }
        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);
        Date date = jwtUtil.extractExpirationDate(token);

        blacklistToken(token, username, date);
    }

    @Override
    public void blacklistToken(String token, String username, Date expirationDate) {
        JwtToken jwtToken = JwtToken.builder()
                .token(token)
                .username(username)
                .expirationDate(expirationDate)
                .build();
        jwtTokenRepository.save(jwtToken);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        JwtToken jwtToken = jwtTokenRepository.findByToken(token);
        return jwtToken != null;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void cleanupExpiredTokens() {
        jwtTokenRepository.deleteAllByExpirationDateLessThan(new Date(System.currentTimeMillis()));
    }

}

