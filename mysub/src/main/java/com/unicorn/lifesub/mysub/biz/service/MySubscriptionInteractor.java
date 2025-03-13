package com.unicorn.lifesub.mysub.biz.service;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.biz.usecase.out.SubscriptionDataAccessInterface;
import com.unicorn.lifesub.mysub.infra.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 마이구독 서비스 유스케이스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MySubscriptionInteractor implements 
    TotalFeeInputBoundary, 
    MySubscriptionsInputBoundary, 
    SubscriptionDetailInputBoundary, 
    SubscribeInputBoundary, 
    CancelSubscriptionInputBoundary,
    SubscriptionCategoriesInputBoundary,
    SubscriptionsByCategoryInputBoundary {

    private final SubscriptionDataAccessInterface subscriptionDataAccess;

    /**
     * 사용자의 총 구독료를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 구독료 응답
     */
    @Override
    @Transactional(readOnly = true)
    public TotalFeeResponse getTotalFee(String userId) {
        log.debug("사용자 {} 총 구독료 조회", userId);
        
        // 사용자의 구독 목록 조회
        List<MySubscription> mySubscriptions = subscriptionDataAccess.findByUserId(userId);
        
        // 총 구독료 계산
        int totalFee = calculateTotalFee(mySubscriptions);
        
        // 범위에 따른 이미지 결정
        String rangeImage = determineRangeImage(totalFee);
        
        return new TotalFeeResponse(totalFee, rangeImage);
    }

    /**
     * 총 구독료를 계산합니다.
     *
     * @param mySubscriptions 내 구독 목록
     * @return 총 구독료
     */
    private int calculateTotalFee(List<MySubscription> mySubscriptions) {
        return mySubscriptions.stream()
                .mapToInt(sub -> {
                    // 구독 서비스 정보 조회
                    Subscription subscription = subscriptionDataAccess.findSubscriptionById(sub.getSubscriptionId())
                            .orElseThrow(() -> new BizException("구독 서비스 정보를 찾을 수 없습니다."));
                    return subscription.getFee();
                })
                .sum();
    }

    /**
     * 구독료 범위에 따른 이미지를 결정합니다.
     *
     * @param totalFee 총 구독료
     * @return 이미지 경로
     */
    private String determineRangeImage(int totalFee) {
        if (totalFee > 200000) {
            return "/images/sub_luxury.png";
        } else if (totalFee > 100000) {
            return "/images/sub_collector.png";
        } else {
            return "/images/sub_lover.png";
        }
    }

    /**
     * 사용자의 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 목록 응답
     */
    @Override
    @Transactional(readOnly = true)
    public List<MySubscriptionListResponse> getMySubscriptions(String userId) {
        log.debug("사용자 {} 구독 목록 조회", userId);
        
        // 사용자의 구독 목록 조회
        List<MySubscription> mySubscriptions = subscriptionDataAccess.findByUserId(userId);
        
        // DTO 변환
        return mySubscriptions.stream()
                .map(sub -> {
                    // 구독 서비스 정보 조회
                    Subscription subscription = subscriptionDataAccess.findSubscriptionById(sub.getSubscriptionId())
                            .orElseThrow(() -> new BizException("구독 서비스 정보를 찾을 수 없습니다."));
                    
                    return new MySubscriptionListResponse(
                            subscription.getId(),
                            subscription.getName(),
                            subscription.getLogoUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 구독 서비스 상세 정보를 조회합니다.
     *
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 상세 정보 응답
     */
    @Override
    @Transactional(readOnly = true)
    public SubscriptionDetailResponse getSubscriptionDetail(Long subscriptionId) {
        log.debug("구독 서비스 {} 상세 정보 조회", subscriptionId);
        
        // 구독 서비스 정보 조회
        Subscription subscription = subscriptionDataAccess.findSubscriptionById(subscriptionId)
                .orElseThrow(() -> new BizException("구독 서비스 정보를 찾을 수 없습니다. ID: " + subscriptionId));
        
        // DTO 변환
        return new SubscriptionDetailResponse(
                subscription.getId(),
                subscription.getName(),
                subscription.getCategoryId(),
                subscription.getDescription(),
                subscription.getFee(),
                subscription.getMaxShareNum()
        );
    }

    /**
     * 구독 서비스를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 응답
     */
    @Override
    @Transactional
    public SubscribeResponse subscribeSub(String userId, Long subscriptionId) {
        log.debug("사용자 {} 구독 서비스 {} 구독 신청", userId, subscriptionId);
        
        // 구독 서비스 존재 여부 확인
        if (!subscriptionDataAccess.findSubscriptionById(subscriptionId).isPresent()) {
            throw new BizException("존재하지 않는 구독 서비스입니다. ID: " + subscriptionId);
        }
        
        // 이미 구독 중인지 확인
        boolean alreadySubscribed = subscriptionDataAccess.findByUserId(userId).stream()
                .anyMatch(sub -> sub.getSubscriptionId().equals(subscriptionId));
                
        if (alreadySubscribed) {
            return new SubscribeResponse(false, "이미 구독 중인 서비스입니다.");
        }
        
        // 구독 정보 저장
        MySubscription mySubscription = new MySubscription(userId, subscriptionId);
        subscriptionDataAccess.save(mySubscription);
        
        return new SubscribeResponse(true, "구독이 완료되었습니다.");
    }

    /**
     * 구독을 취소합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 성공 응답
     */
    @Override
    @Transactional
    public SuccessResponse cancelSub(String userId, Long subscriptionId) {
        log.debug("사용자 {} 구독 서비스 {} 구독 취소", userId, subscriptionId);
        
        // 구독 정보 삭제
        subscriptionDataAccess.delete(userId, subscriptionId);
        
        return new SuccessResponse(true, "구독이 취소되었습니다.");
    }

    /**
     * 구독 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryListDTO> getCategories() {
        log.debug("구독 카테고리 목록 조회");
        
        // 모든 카테고리 조회
        List<Category> categories = subscriptionDataAccess.findAllCategories();
        
        // DTO 변환
        return categories.stream()
                .map(category -> new CategoryListDTO(category.getCategoryName()))
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionListResponse> getSubscriptionsByCategory(String categoryId) {
        log.debug("카테고리 {} 구독 서비스 목록 조회", categoryId);
        
        // 카테고리별 구독 서비스 목록 조회
        List<Subscription> subscriptions = subscriptionDataAccess.findSubscriptionsByCategory(categoryId);
        
        // DTO 변환
        return subscriptions.stream()
                .map(subscription -> new SubscriptionListResponse(
                        subscription.getId(),
                        subscription.getName(),
                        subscription.getLogoUrl(),
                        subscription.getDescription(),
                        subscription.getFee()
                ))
                .collect(Collectors.toList());
    }
}
