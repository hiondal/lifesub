package com.unicorn.lifesub.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오류 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "오류 응답")
public class ErrorResponse {

    @Schema(description = "오류 코드", example = "400")
    private int code;

    @Schema(description = "오류 메시지", example = "유효하지 않은 요청입니다.")
    private String message;
}
