package com.unicorn.lifesub.mysub.infra.gateway.entity;

import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 내 구독 엔티티 클래스입니다.
 */
@Entity
@Table(name = "my_subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MySubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 내 구독 도메인 객체
     */
    public MySubscription toDomain() {
        return new MySubscription(userId, subscriptionId);
    }

    /**
     * 도메인 객체로부터 엔티티를 생성합니다.
     *
     * @param mySubscription 내 구독 도메인 객체
     * @return 내 구독 엔티티
     */
    public static MySubscriptionEntity fromDomain(MySubscription mySubscription) {
        return MySubscriptionEntity.builder()
                .userId(mySubscription.getUserId())
                .subscriptionId(mySubscription.getSubscriptionId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
