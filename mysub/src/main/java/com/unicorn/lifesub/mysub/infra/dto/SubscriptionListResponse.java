package com.unicorn.lifesub.mysub.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 서비스 목록 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "구독 서비스 목록 응답")
public class SubscriptionListResponse {

    @Schema(description = "구독 서비스 ID", example = "1")
    private Long subscriptionId;

    @Schema(description = "서비스 이름", example = "넷플릭스")
    private String serviceName;

    @Schema(description = "로고 URL", example = "/images/netflix_logo.png")
    private String logoUrl;

    @Schema(description = "설명", example = "다양한 영화와 드라마를 볼 수 있는 스트리밍 서비스")
    private String description;

    @Schema(description = "구독료", example = "13900")
    private int fee;
}
