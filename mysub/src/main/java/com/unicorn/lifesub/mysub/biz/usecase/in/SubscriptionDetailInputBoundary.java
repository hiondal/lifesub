package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.SubscriptionDetailResponse;

/**
 * 구독 상세 정보 조회 유스케이스 인터페이스입니다.
 */
public interface SubscriptionDetailInputBoundary {
    
    /**
     * 구독 서비스 상세 정보를 조회합니다.
     *
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 상세 정보 응답
     */
    SubscriptionDetailResponse getSubscriptionDetail(Long subscriptionId);
}
