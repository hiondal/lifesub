package com.unicorn.lifesub.mysub.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 총 구독료 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "총 구독료 응답")
public class TotalFeeResponse {

    @Schema(description = "총 구독료", example = "45000")
    private int totalFee;

    @Schema(description = "범위에 따른 이미지 URL", example = "/images/sub_collector.png")
    private String rangeImage;
}
