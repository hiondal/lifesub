package com.unicorn.lifesub.recommend.repository;

/**
 * 지출 정보 저장소 인터페이스입니다.
 */
public interface SpendingRepository {

    /**
     * 사용자의 최고 지출 카테고리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 최고 지출 카테고리
     */
    String findTopCategoryByUserId(String userId);

    /**
     * 사용자의 총 지출액을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 지출액
     */
    int findTotalExpenseByUserId(String userId);
}
