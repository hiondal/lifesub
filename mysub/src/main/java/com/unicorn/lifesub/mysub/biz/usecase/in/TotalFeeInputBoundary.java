package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.TotalFeeResponse;

/**
 * 총 구독료 조회 유스케이스 인터페이스입니다.
 */
public interface TotalFeeInputBoundary {
    
    /**
     * 사용자의 총 구독료를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 구독료 응답
     */
    TotalFeeResponse getTotalFee(String userId);
}
