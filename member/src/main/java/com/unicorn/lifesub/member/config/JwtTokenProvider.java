package com.unicorn.lifesub.member.config;

import com.unicorn.lifesub.member.dto.TokenResponse;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT 토큰 제공자 클래스입니다.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    private Key key;

    // 토큰 무효화를 위한 저장소 (실제 환경에서는 Redis 등을 사용)
    private final Map<String, Boolean> blacklistedTokens = new HashMap<>();

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    /**
     * 토큰을 생성합니다.
     *
     * @param memberEntity 회원 엔티티
     * @param authorities 권한 목록
     * @return 토큰 응답 DTO
     */
    public TokenResponse createToken(MemberEntity memberEntity, Collection<GrantedAuthority> authorities) {
        String userId = memberEntity.getUserId();
        Claims claims = Jwts.claims().setSubject(userId);
        
        // 권한 정보 추가
        claims.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")));
                
        Date now = new Date();
        Date accessTokenValidity = new Date(now.getTime() + this.accessTokenValidity * 1000);
        Date refreshTokenValidity = new Date(now.getTime() + this.refreshTokenValidity * 1000);
        
        // 액세스 토큰 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
                
        // 리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(refreshTokenValidity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
                
        return new TokenResponse(accessToken, refreshToken, this.accessTokenValidity);
    }

    /**
     * 토큰의 유효성을 검사합니다.
     *
     * @param token JWT 토큰
     * @return 유효성 여부 (0: 유효, -1: 만료, -2: 기타 오류)
     */
    public int validateToken(String token) {
        try {
            // 블랙리스트 토큰 확인
            if (blacklistedTokens.containsKey(token) && blacklistedTokens.get(token)) {
                log.warn("블랙리스트에 등록된 토큰입니다.");
                return -2;
            }
            
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return 0;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.", e);
            return -1;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 JWT 토큰입니다.", e);
            return -2;
        }
    }

    /**
     * 토큰에서 인증 정보를 추출합니다.
     *
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
                
        String userId = claims.getSubject();
        String roles = claims.get("roles", String.class);
        
        // 사용자 정보 생성
        UserDetails userDetails = User.builder()
                .username(userId)
                .password("")
                .roles(roles.split(","))
                .build();
                
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰을 무효화합니다.
     *
     * @param userId 사용자 ID
     */
    public void invalidateToken(String userId) {
        // 실제 환경에서는 Redis 등을 사용하여 토큰 무효화 처리
        log.debug("사용자 {} 토큰 무효화", userId);
    }
}
