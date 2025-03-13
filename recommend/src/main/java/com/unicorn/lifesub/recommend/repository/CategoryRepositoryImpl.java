package com.unicorn.lifesub.recommend.repository;

import com.unicorn.lifesub.recommend.repository.entity.CategoryEntity;
import com.unicorn.lifesub.recommend.repository.jpa.CategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 카테고리 저장소 구현체입니다.
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    /**
     * 지출 카테고리에 매핑된 구독 카테고리를 조회합니다.
     *
     * @param spendingCategory 지출 카테고리
     * @return 카테고리 엔티티 (Optional)
     */
    @Override
    public Optional<CategoryEntity> findBySpendingCategory(String spendingCategory) {
        return categoryJpaRepository.findBySpendingCategory(spendingCategory);
    }
}
