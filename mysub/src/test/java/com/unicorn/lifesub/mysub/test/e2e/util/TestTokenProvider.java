package com.unicorn.lifesub.mysub.test.e2e.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * E2E 테스트를 위한 JWT 토큰을 생성하는 유틸리티 클래스입니다.
 */
@Component
public class TestTokenProvider {

    @Value("${jwt.secret-key:defaultSecretKey}")
    private String secretKey;

    private Key key;

    /**
     * 테스트용 JWT 토큰을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 JWT 토큰
     */
    public String createToken(String userId) {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes());
        }

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600 * 1000); // 1시간

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("roles", "ROLE_USER")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}