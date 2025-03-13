package com.unicorn.lifesub.recommend.repository;

import com.unicorn.lifesub.recommend.repository.jpa.SpendingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 지출 정보 저장소 구현체입니다.
 */
@Repository
@RequiredArgsConstructor
public class SpendingRepositoryImpl implements SpendingRepository {

    private final SpendingJpaRepository spendingJpaRepository;

    /**
     * 사용자의 최고 지출 카테고리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 최고 지출 카테고리
     */
    @Override
    public String findTopCategoryByUserId(String userId) {
        return spendingJpaRepository.findTopCategoryByUserId(userId);
    }

    /**
     * 사용자의 총 지출액을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 지출액
     */
    @Override
    public int findTotalExpenseByUserId(String userId) {
        return spendingJpaRepository.findTotalExpenseByUserId(userId);
    }
}
