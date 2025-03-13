package com.unicorn.lifesub.member.service;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.member.config.JwtTokenProvider;
import com.unicorn.lifesub.member.domain.Member;
import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.dto.TokenResponse;
import com.unicorn.lifesub.member.repository.MemberRepository;
import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 인증 관련 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequest 로그인 요청 정보
     * @return 발급된 토큰 정보
     */
    @Override
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        log.debug("로그인 시도: {}", loginRequest.getUserId());
        
        // 사용자 정보 조회
        MemberEntity memberEntity = memberRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다."));
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), memberEntity.getPassword())) {
            log.warn("비밀번호 불일치: {}", loginRequest.getUserId());
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        
        // 토큰 발급
        return jwtTokenProvider.createToken(
                memberEntity, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param logoutRequest 로그아웃 요청 정보
     * @return 로그아웃 결과
     */
    @Override
    @Transactional
    public LogoutResponse logout(LogoutRequest logoutRequest) {
        log.debug("로그아웃 시도: {}", logoutRequest.getUserId());
        
        // 사용자 정보 확인
        MemberEntity memberEntity = memberRepository.findByUserId(logoutRequest.getUserId())
                .orElseThrow(() -> new BizException("존재하지 않는 사용자입니다."));
        
        // 토큰 무효화
        jwtTokenProvider.invalidateToken(logoutRequest.getUserId());
        
        return new LogoutResponse(true, "로그아웃되었습니다.");
    }
}
