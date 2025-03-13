package com.unicorn.lifesub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그아웃 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그아웃 응답")
public class LogoutResponse {

    @Schema(description = "성공 여부", example = "true")
    private boolean success;

    @Schema(description = "메시지", example = "로그아웃되었습니다.")
    private String message;
}
