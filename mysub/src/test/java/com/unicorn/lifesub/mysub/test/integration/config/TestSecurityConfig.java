package com.unicorn.lifesub.mysub.test.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 통합 테스트용 보안 설정 클래스입니다.
 */
@Configuration
@Profile("integration-test")
public class TestSecurityConfig {

    /**
     * 통합 테스트를 위한 보안 필터 체인을 구성합니다.
     *
     * @param http HTTP 보안 설정
     * @return SecurityFilterChain 인스턴스
     * @throws Exception 필터 체인 생성 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}