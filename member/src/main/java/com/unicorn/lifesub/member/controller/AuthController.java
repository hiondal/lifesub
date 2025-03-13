package com.unicorn.lifesub.member.controller;

import com.unicorn.lifesub.member.dto.LoginRequest;
import com.unicorn.lifesub.member.dto.LogoutRequest;
import com.unicorn.lifesub.member.dto.LogoutResponse;
import com.unicorn.lifesub.member.dto.TokenResponse;
import com.unicorn.lifesub.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 회원 인증 관련 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인, 로그아웃 등 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginRequest 로그인 요청 정보
     * @return 발급된 토큰 정보
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 ID와 비밀번호로 로그인하여 인증 토큰을 발급받습니다.")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param logoutRequest 로그아웃 요청 정보
     * @return 로그아웃 결과
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자를 로그아웃 처리하고 발급된 토큰을 무효화합니다.")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        LogoutResponse logoutResponse = authService.logout(logoutRequest);
        return ResponseEntity.ok(logoutResponse);
    }
}
