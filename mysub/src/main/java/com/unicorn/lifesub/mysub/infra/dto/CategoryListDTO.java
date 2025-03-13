package com.unicorn.lifesub.mysub.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 목록 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "카테고리 목록 DTO")
public class CategoryListDTO {

    @Schema(description = "카테고리 이름", example = "비디오 스트리밍")
    private String categoryName;
}
