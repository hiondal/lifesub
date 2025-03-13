package com.unicorn.lifesub.recommend.service;

import com.unicorn.lifesub.recommend.dto.RecommendCategoryDTO;

/**
 * 구독추천 서비스 인터페이스입니다.
 */
public interface RecommendService {

    /**
     * 최고 지출 카테고리 기반 구독 추천을 제공합니다.
     *
     * @param userId 사용자 ID
     * @return 추천 카테고리 응답
     */
    RecommendCategoryDTO getRecommendCategories(String userId);
}
