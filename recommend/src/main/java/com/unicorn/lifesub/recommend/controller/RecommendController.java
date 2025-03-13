package com.unicorn.lifesub.recommend.controller;

import com.unicorn.lifesub.recommend.dto.RecommendCategoryDTO;
import com.unicorn.lifesub.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 구독추천 서비스 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
@Tag(name = "구독추천 API", description = "지출 기반 구독 추천 API")
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 최고 지출 카테고리 기반 구독 추천을 제공합니다.
     *
     * @param userId 사용자 ID
     * @return 추천 카테고리 응답
     */
    @GetMapping("/categories")
    @Operation(summary = "추천 구독 카테고리 조회", description = "최고 지출 카테고리 기반 구독 추천을 제공합니다.")
    public ResponseEntity<RecommendCategoryDTO> getRecommendCategories(
            @Parameter(description = "사용자 ID", required = true)
            @RequestParam String userId) {
        RecommendCategoryDTO response = recommendService.getRecommendCategories(userId);
        return ResponseEntity.ok(response);
    }
}
