package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.SubscribeResponse;

/**
 * 구독하기 유스케이스 인터페이스입니다.
 */
public interface SubscribeInputBoundary {
    
    /**
     * 구독 서비스를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 응답
     */
    SubscribeResponse subscribeSub(String userId, Long subscriptionId);
}
