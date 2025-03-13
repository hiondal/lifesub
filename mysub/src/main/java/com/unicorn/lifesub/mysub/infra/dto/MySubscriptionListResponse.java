package com.unicorn.lifesub.mysub.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 목록 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "구독 목록 응답")
public class MySubscriptionListResponse {

    @Schema(description = "구독 서비스 ID", example = "1")
    private Long subscriptionId;

    @Schema(description = "서비스 이름", example = "넷플릭스")
    private String serviceName;

    @Schema(description = "로고 URL", example = "/images/netflix_logo.png")
    private String logoUrl;
}
