package com.unicorn.lifesub.member.service;

import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.dto.TokenResponse;

/**
 * 인증 관련 서비스 인터페이스입니다.
 */
public interface AuthService {

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequest 로그인 요청 정보
     * @return 발급된 토큰 정보
     */
    TokenResponse login(LoginRequest loginRequest);

    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param logoutRequest 로그아웃 요청 정보
     * @return 로그아웃 결과
     */
    LogoutResponse logout(LogoutRequest logoutRequest);
}
