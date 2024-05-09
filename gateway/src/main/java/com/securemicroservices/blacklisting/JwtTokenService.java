package com.securemicroservices.blacklisting;

import java.util.Date;

public interface JwtTokenService {
    void logout(String token);
    void blacklistToken(String token, String username, Date expirationDate);

    boolean isTokenBlacklisted(String token);
}

