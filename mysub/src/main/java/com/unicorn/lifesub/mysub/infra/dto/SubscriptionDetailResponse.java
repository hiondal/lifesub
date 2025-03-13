package com.unicorn.lifesub.mysub.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 상세 정보 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "구독 상세 정보 응답")
public class SubscriptionDetailResponse {

    @Schema(description = "구독 서비스 ID", example = "1")
    private Long subscriptionId;

    @Schema(description = "서비스 이름", example = "넷플릭스")
    private String serviceName;

    @Schema(description = "카테고리", example = "비디오 스트리밍")
    private String category;

    @Schema(description = "설명", example = "다양한 영화와 드라마를 볼 수 있는 스트리밍 서비스")
    private String description;

    @Schema(description = "구독료", example = "13900")
    private int fee;

    @Schema(description = "최대 공유 인원 수", example = "4")
    private int maxShareNum;
}
