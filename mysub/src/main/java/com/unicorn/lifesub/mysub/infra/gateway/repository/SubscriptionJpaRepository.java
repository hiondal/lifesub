package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 구독 서비스 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    
    /**
     * 카테고리 ID로 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 엔티티 목록
     */
    List<SubscriptionEntity> findByCategoryId(String categoryId);
}
