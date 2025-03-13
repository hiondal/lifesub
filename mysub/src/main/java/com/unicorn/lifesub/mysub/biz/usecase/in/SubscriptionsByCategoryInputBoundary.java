package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.SubscriptionListResponse;

import java.util.List;

/**
 * 카테고리별 구독 서비스 목록 조회 유스케이스 인터페이스입니다.
 */
public interface SubscriptionsByCategoryInputBoundary {
    
    /**
     * 카테고리별 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 목록
     */
    List<SubscriptionListResponse> getSubscriptionsByCategory(String categoryId);
}
