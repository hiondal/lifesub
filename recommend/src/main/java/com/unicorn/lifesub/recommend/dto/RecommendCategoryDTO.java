package com.unicorn.lifesub.recommend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 추천 카테고리 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "추천 카테고리 응답")
public class RecommendCategoryDTO {

    @Schema(description = "카테고리 이름", example = "비디오 스트리밍")
    private String categoryName;

    @Schema(description = "이미지 경로", example = "/images/video_streaming.png")
    private String imagePath;

    @Schema(description = "기준일", example = "2023-06-01")
    private String baseDate;
}
