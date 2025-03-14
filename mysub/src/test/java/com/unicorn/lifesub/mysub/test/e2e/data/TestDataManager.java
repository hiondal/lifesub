package com.unicorn.lifesub.mysub.test.e2e.support;

import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.CategoryJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.MySubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * E2E 테스트를 위한 테스트 데이터를 관리하는 클래스입니다.
 */
@Component
public class TestDataManager {

    @Autowired
    private CategoryJpaRepository categoryRepository;

    @Autowired
    private SubscriptionJpaRepository subscriptionRepository;

    @Autowired
    private MySubscriptionJpaRepository mySubscriptionRepository;

    @Value("${test.user.id}")
    private String TEST_USER_ID;

    @Value("${jwt.secret-key}")
    private String secretKey;

    /**
     * 테스트용 JWT 토큰을 생성합니다.
     *
     * @return 생성된 JWT 토큰
     */
    public String getTestUserToken() {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 3600 * 1000); // 1시간

        return Jwts.builder()
                .setSubject(TEST_USER_ID)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("roles", "ROLE_USER")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 테스트 데이터를 초기화합니다.
     */
    @Transactional
    public void setupTestData() {
        // 카테고리 데이터 설정
        setupCategories();

        // 구독 서비스 데이터 설정
        List<SubscriptionEntity> subscriptions = setupSubscriptions();

        // 사용자 구독 데이터 설정
        setupUserSubscriptions(subscriptions);
    }

    /**
     * 테스트용 구독 카테고리 데이터를 설정합니다.
     *
     * @return 생성된 카테고리 엔티티 목록
     */
    @Transactional
    public List<CategoryEntity> setupCategories() {
        // 기존 카테고리 삭제
        categoryRepository.deleteAll();

        List<CategoryEntity> categories = Arrays.asList(
                CategoryEntity.builder()
                        .categoryId("OTT")
                        .categoryName("OTT/동영상")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("MUSIC")
                        .categoryName("음악")
                        .build(),
                CategoryEntity.builder()
                        .categoryId("FOOD")
                        .categoryName("식품")
                        .build()
        );

        return categoryRepository.saveAll(categories);
    }

    /**
     * 테스트용 구독 서비스 데이터를 설정합니다.
     *
     * @return 생성된 구독 서비스 엔티티 목록
     */
    @Transactional
    public List<SubscriptionEntity> setupSubscriptions() {
        // 기존 구독 서비스 삭제
        subscriptionRepository.deleteAll();

        List<SubscriptionEntity> subscriptions = Arrays.asList(
                SubscriptionEntity.builder()
                        .name("넷플릭스")
                        .description("글로벌 스트리밍 서비스")
                        .categoryId("OTT")
                        .fee(17000)
                        .maxShareNum(4)
                        .logoUrl("/images/netflix.png")
                        .build(),
                SubscriptionEntity.builder()
                        .name("유튜브 프리미엄")
                        .description("광고 없는 유튜브 시청")
                        .categoryId("OTT")
                        .fee(14900)
                        .maxShareNum(6)
                        .logoUrl("/images/youtube.png")
                        .build(),
                SubscriptionEntity.builder()
                        .name("멜론")
                        .description("국내 최대 음원 스트리밍")
                        .categoryId("MUSIC")
                        .fee(10900)
                        .maxShareNum(1)
                        .logoUrl("/images/melon.png")
                        .build(),
                SubscriptionEntity.builder()
                        .name("쿠팡이츠")
                        .description("식품 정기배송 서비스")
                        .categoryId("FOOD")
                        .fee(4900)
                        .maxShareNum(1)
                        .logoUrl("/images/coupang-eats.png")
                        .build()
        );

        return subscriptionRepository.saveAll(subscriptions);
    }

    /**
     * 테스트용 사용자 구독 데이터를 설정합니다.
     *
     * @param subscriptions 구독 서비스 목록
     * @return 생성된 내 구독 엔티티 목록
     */
    @Transactional
    public List<MySubscriptionEntity> setupUserSubscriptions(List<SubscriptionEntity> subscriptions) {
        // 기존 사용자 구독 데이터 삭제
        mySubscriptionRepository.deleteAll();

        // 테스트 데이터로 첫 번째와 세 번째 구독 서비스를 사용자가 구독 중이라고 가정
        List<MySubscriptionEntity> userSubscriptions = Arrays.asList(
                MySubscriptionEntity.builder()
                        .userId(TEST_USER_ID)
                        .subscriptionId(subscriptions.get(0).getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                MySubscriptionEntity.builder()
                        .userId(TEST_USER_ID)
                        .subscriptionId(subscriptions.get(2).getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return mySubscriptionRepository.saveAll(userSubscriptions);
    }

    /**
     * 특정 사용자의 구독 중인 서비스 ID 목록을 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 중인 서비스 ID 목록
     */
    @Transactional
    public List<Long> getUserSubscriptionIds(String userId) {
        List<MySubscriptionEntity> userSubs = mySubscriptionRepository.findByUserId(userId);
        return userSubs.stream()
                .map(MySubscriptionEntity::getSubscriptionId)
                .collect(Collectors.toList());
    }

    /**
     * 사용자가 구독하지 않은 구독 서비스 ID를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 구독하지 않은 구독 서비스 ID (없으면 null)
     */
    @Transactional
    public Long getUnsubscribedServiceId(String userId) {
        List<Long> subscribedIds = getUserSubscriptionIds(userId);
        return subscriptionRepository.findAll().stream()
                .map(SubscriptionEntity::getId)
                .filter(id -> !subscribedIds.contains(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * 사용자가 구독 중인 구독 서비스 ID를 반환합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 중인 구독 서비스 ID (없으면 null)
     */
    @Transactional
    public Long getSubscribedServiceId(String userId) {
        List<Long> subscribedIds = getUserSubscriptionIds(userId);
        return subscribedIds.isEmpty() ? null : subscribedIds.get(0);
    }

    /**
     * 테스트용 사용자의 구독 서비스 총 금액을 계산합니다.
     *
     * @return 총 구독료
     */
    @Transactional
    public int calculateTotalFee() {
        List<Long> subscribedIds = getUserSubscriptionIds(TEST_USER_ID);
        return subscribedIds.stream()
                .map(id -> subscriptionRepository.findById(id).orElseThrow())
                .mapToInt(SubscriptionEntity::getFee)
                .sum();
    }

    /**
     * 테스트 구독 서비스를 반환합니다.
     *
     * @return 구독 서비스 엔티티
     */
    @Transactional
    public SubscriptionEntity getTestSubscription() {
        return subscriptionRepository.findAll().get(0);
    }

    /**
     * 카테고리 수를 반환합니다.
     *
     * @return 카테고리 수
     */
    @Transactional
    public int getCategoryCount() {
        return (int) categoryRepository.count();
    }

    /**
     * 카테고리별 구독 서비스 수를 반환합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 수
     */
    @Transactional
    public int getSubscriptionCountByCategory(String categoryId) {
        return subscriptionRepository.findByCategoryId(categoryId).size();
    }
}