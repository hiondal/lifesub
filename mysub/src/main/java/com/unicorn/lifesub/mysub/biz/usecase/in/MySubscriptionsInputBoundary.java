package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.MySubscriptionListResponse;

import java.util.List;

/**
 * 내 구독 목록 조회 유스케이스 인터페이스입니다.
 */
public interface MySubscriptionsInputBoundary {
    
    /**
     * 사용자의 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 목록 응답
     */
    List<MySubscriptionListResponse> getMySubscriptions(String userId);
}
