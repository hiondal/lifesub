package com.unicorn.lifesub.recommend.repository.jpa;

import com.unicorn.lifesub.recommend.repository.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 카테고리 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {

    /**
     * 지출 카테고리에 매핑된 구독 카테고리를 조회합니다.
     *
     * @param spendingCategory 지출 카테고리
     * @return 카테고리 엔티티 (Optional)
     */
    Optional<CategoryEntity> findBySpendingCategory(String spendingCategory);
}
