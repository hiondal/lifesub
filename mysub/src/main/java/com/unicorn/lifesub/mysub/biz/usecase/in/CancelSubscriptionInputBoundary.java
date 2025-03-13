package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.SuccessResponse;

/**
 * 구독 취소 유스케이스 인터페이스입니다.
 */
public interface CancelSubscriptionInputBoundary {
    
    /**
     * 구독을 취소합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 성공 응답
     */
    SuccessResponse cancelSub(String userId, Long subscriptionId);
}
