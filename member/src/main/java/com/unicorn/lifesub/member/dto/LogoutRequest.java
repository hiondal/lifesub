package com.unicorn.lifesub.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그아웃 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그아웃 요청")
public class LogoutRequest {

    @NotBlank(message = "사용자 ID는 필수입니다.")
    @Schema(description = "사용자 ID", example = "user01")
    private String userId;
}
