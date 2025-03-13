package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 카테고리 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {
}
