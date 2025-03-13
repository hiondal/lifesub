package com.unicorn.lifesub.mysub.infra.gateway.entity;

import com.unicorn.lifesub.mysub.biz.domain.Subscription;
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

/**
 * 구독 서비스 엔티티 클래스입니다.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int fee;

    @Column(name = "max_share_num")
    private int maxShareNum;

    @Column(name = "logo_url")
    private String logoUrl;

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 구독 서비스 도메인 객체
     */
    public Subscription toDomain() {
        return new Subscription(
                id,
                name,
                categoryId,
                description,
                fee,
                maxShareNum,
                logoUrl
        );
    }

    /**
     * 도메인 객체로부터 엔티티를 생성합니다.
     *
     * @param subscription 구독 서비스 도메인 객체
     * @return 구독 서비스 엔티티
     */
    public static SubscriptionEntity fromDomain(Subscription subscription) {
        return SubscriptionEntity.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .categoryId(subscription.getCategoryId())
                .description(subscription.getDescription())
                .fee(subscription.getFee())
                .maxShareNum(subscription.getMaxShareNum())
                .logoUrl(subscription.getLogoUrl())
                .build();
    }
}
