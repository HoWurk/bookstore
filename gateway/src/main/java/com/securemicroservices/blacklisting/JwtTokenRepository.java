package com.securemicroservices.blacklisting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    JwtToken findByToken(String token);
    void deleteAllByExpirationDateLessThan(Date date);
}

