package com.unicorn.lifesub.mysub.biz.usecase.out;

import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;

import java.util.List;
import java.util.Optional;

/**
 * 구독 데이터 접근 인터페이스입니다.
 */
public interface SubscriptionDataAccessInterface {
    
    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 내 구독 목록
     */
    List<MySubscription> findByUserId(String userId);
    
    /**
     * 구독 서비스 ID로 구독 정보를 조회합니다.
     *
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 정보 (Optional)
     */
    Optional<Subscription> findSubscriptionById(Long subscriptionId);
    
    /**
     * 내 구독 정보를 저장합니다.
     *
     * @param mySubscription 내 구독 도메인 객체
     * @return 저장된 내 구독 정보
     */
    MySubscription save(MySubscription mySubscription);
    
    /**
     * 구독을 취소합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     */
    void delete(String userId, Long subscriptionId);
    
    /**
     * 모든 카테고리를 조회합니다.
     *
     * @return 카테고리 목록
     */
    List<Category> findAllCategories();
    
    /**
     * 카테고리별 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 목록
     */
    List<Subscription> findSubscriptionsByCategory(String categoryId);
}
