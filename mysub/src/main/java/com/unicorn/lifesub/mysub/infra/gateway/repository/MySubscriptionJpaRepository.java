package com.unicorn.lifesub.mysub.infra.gateway.repository;

import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 내 구독 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface MySubscriptionJpaRepository extends JpaRepository<MySubscriptionEntity, Long> {
    
    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 내 구독 엔티티 목록
     */
    List<MySubscriptionEntity> findByUserId(String userId);
    
    /**
     * 사용자 ID와 구독 서비스 ID로 구독 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 내 구독 엔티티 (Optional)
     */
    Optional<MySubscriptionEntity> findByUserIdAndSubscriptionId(String userId, Long subscriptionId);
}
