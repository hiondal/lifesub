package com.unicorn.lifesub.recommend.repository.jpa;

import com.unicorn.lifesub.recommend.repository.entity.SpendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 지출 정보 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface SpendingJpaRepository extends JpaRepository<SpendingEntity, Long> {

    /**
     * 사용자의 최고 지출 카테고리를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 최고 지출 카테고리
     */
    @Query("SELECT s.category FROM SpendingEntity s WHERE s.userId = ?1 " +
            "GROUP BY s.category ORDER BY SUM(s.amount) DESC LIMIT 1")
    String findTopCategoryByUserId(String userId);

    /**
     * 사용자의 총 지출액을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 지출액
     */
    @Query("SELECT CAST(SUM(s.amount) AS int) FROM SpendingEntity s WHERE s.userId = ?1")
    int findTotalExpenseByUserId(String userId);
}
