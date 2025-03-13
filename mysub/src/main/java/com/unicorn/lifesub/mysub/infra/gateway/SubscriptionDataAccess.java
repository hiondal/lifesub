package com.unicorn.lifesub.mysub.infra.gateway;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.usecase.out.SubscriptionDataAccessInterface;
import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.CategoryJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.MySubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 구독 데이터 접근 구현체입니다.
 */
@Component
@RequiredArgsConstructor
public class SubscriptionDataAccess implements SubscriptionDataAccessInterface {

    private final MySubscriptionJpaRepository mySubscriptionJpaRepository;
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 내 구독 목록
     */
    @Override
    public List<MySubscription> findByUserId(String userId) {
        List<MySubscriptionEntity> entities = mySubscriptionJpaRepository.findByUserId(userId);
        return entities.stream()
                .map(MySubscriptionEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 구독 서비스 ID로 구독 정보를 조회합니다.
     *
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 정보 (Optional)
     */
    @Override
    public Optional<Subscription> findSubscriptionById(Long subscriptionId) {
        return subscriptionJpaRepository.findById(subscriptionId)
                .map(SubscriptionEntity::toDomain);
    }

    /**
     * 내 구독 정보를 저장합니다.
     *
     * @param mySubscription 내 구독 도메인 객체
     * @return 저장된 내 구독 정보
     */
    @Override
    public MySubscription save(MySubscription mySubscription) {
        MySubscriptionEntity entity = MySubscriptionEntity.fromDomain(mySubscription);
        MySubscriptionEntity savedEntity = mySubscriptionJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    /**
     * 구독을 취소합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     */
    @Override
    public void delete(String userId, Long subscriptionId) {
        MySubscriptionEntity entity = mySubscriptionJpaRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new BizException("구독 정보를 찾을 수 없습니다."));
                
        mySubscriptionJpaRepository.delete(entity);
    }

    /**
     * 모든 카테고리를 조회합니다.
     *
     * @return 카테고리 목록
     */
    @Override
    public List<Category> findAllCategories() {
        List<CategoryEntity> entities = categoryJpaRepository.findAll();
        return entities.stream()
                .map(CategoryEntity::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 목록
     */
    @Override
    public List<Subscription> findSubscriptionsByCategory(String categoryId) {
        List<SubscriptionEntity> entities = subscriptionJpaRepository.findByCategoryId(categoryId);
        return entities.stream()
                .map(SubscriptionEntity::toDomain)
                .collect(Collectors.toList());
    }
}
