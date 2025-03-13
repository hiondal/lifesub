package com.unicorn.lifesub.recommend.repository;

import com.unicorn.lifesub.recommend.repository.entity.CategoryEntity;

import java.util.Optional;

/**
 * 카테고리 저장소 인터페이스입니다.
 */
public interface CategoryRepository {

    /**
     * 지출 카테고리에 매핑된 구독 카테고리를 조회합니다.
     *
     * @param spendingCategory 지출 카테고리
     * @return 카테고리 엔티티 (Optional)
     */
    Optional<CategoryEntity> findBySpendingCategory(String spendingCategory);
}
