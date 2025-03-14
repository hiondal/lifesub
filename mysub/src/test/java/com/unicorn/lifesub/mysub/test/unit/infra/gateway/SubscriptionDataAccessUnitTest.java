package com.unicorn.lifesub.mysub.test.unit.infra.gateway;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.infra.gateway.SubscriptionDataAccess;
import com.unicorn.lifesub.mysub.infra.gateway.entity.CategoryEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.MySubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.CategoryJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.MySubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.infra.gateway.repository.SubscriptionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * SubscriptionDataAccess 단위 테스트 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionDataAccessUnitTest {

    @Mock
    private MySubscriptionJpaRepository mySubscriptionJpaRepository;

    @Mock
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @InjectMocks
    private SubscriptionDataAccess subscriptionDataAccess;

    private static final String TEST_USER_ID = "user01";
    private static final Long TEST_SUBSCRIPTION_ID = 1L;
    private static final String TEST_CATEGORY_ID = "OTT";

    private MySubscriptionEntity testMySubscriptionEntity;
    private SubscriptionEntity testSubscriptionEntity;
    private CategoryEntity testCategoryEntity;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        testMySubscriptionEntity = new MySubscriptionEntity(
                1L,
                TEST_USER_ID,
                TEST_SUBSCRIPTION_ID,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        testSubscriptionEntity = new SubscriptionEntity(
                TEST_SUBSCRIPTION_ID,
                "Netflix",
                "OTT",
                "Streaming service",
                13900,
                4,
                "/images/netflix.png"
        );

        testCategoryEntity = new CategoryEntity(TEST_CATEGORY_ID, "OTT/동영상");
    }

    @Test
    @DisplayName("사용자 ID로 구독 목록 조회 시 정확한 도메인 객체로 변환해야 함")
    void givenUserId_whenFindByUserId_thenReturnDomainObjects() {
        // Given
        List<MySubscriptionEntity> entities = Collections.singletonList(testMySubscriptionEntity);
        given(mySubscriptionJpaRepository.findByUserId(TEST_USER_ID)).willReturn(entities);

        // When
        List<MySubscription> result = subscriptionDataAccess.findByUserId(TEST_USER_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(result.get(0).getSubscriptionId()).isEqualTo(TEST_SUBSCRIPTION_ID);

        // 메소드 호출 검증
        verify(mySubscriptionJpaRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("구독 서비스 ID로 조회 시 정확한 도메인 객체로 변환해야 함")
    void givenSubscriptionId_whenFindSubscriptionById_thenReturnDomainObject() {
        // Given
        given(subscriptionJpaRepository.findById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscriptionEntity));

        // When
        Optional<Subscription> result = subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(result).isPresent();
        Subscription subscription = result.get();
        assertThat(subscription.getId()).isEqualTo(TEST_SUBSCRIPTION_ID);
        assertThat(subscription.getName()).isEqualTo("Netflix");
        assertThat(subscription.getCategoryId()).isEqualTo("OTT");
        assertThat(subscription.getDescription()).isEqualTo("Streaming service");
        assertThat(subscription.getFee()).isEqualTo(13900);
        assertThat(subscription.getMaxShareNum()).isEqualTo(4);
        assertThat(subscription.getLogoUrl()).isEqualTo("/images/netflix.png");

        // 메소드 호출 검증
        verify(subscriptionJpaRepository).findById(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("내 구독 정보 저장 시 정확한 도메인 객체로 변환해야 함")
    void givenMySubscription_whenSave_thenReturnSavedDomainObject() {
        // Given
        MySubscription mySubscription = new MySubscription(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
        given(mySubscriptionJpaRepository.save(any(MySubscriptionEntity.class))).willReturn(testMySubscriptionEntity);

        // When
        MySubscription result = subscriptionDataAccess.save(mySubscription);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(TEST_USER_ID);
        assertThat(result.getSubscriptionId()).isEqualTo(TEST_SUBSCRIPTION_ID);

        // 메소드 호출 검증
        verify(mySubscriptionJpaRepository).save(any(MySubscriptionEntity.class));
    }

    @Test
    @DisplayName("구독 취소 시 저장소에서 해당 구독 정보를 삭제해야 함")
    void givenUserIdAndSubscriptionId_whenDelete_thenDeleteFromRepository() {
        // Given
        given(mySubscriptionJpaRepository.findByUserIdAndSubscriptionId(TEST_USER_ID, TEST_SUBSCRIPTION_ID))
                .willReturn(Optional.of(testMySubscriptionEntity));

        // When
        subscriptionDataAccess.delete(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        // 메소드 호출 검증
        verify(mySubscriptionJpaRepository).findByUserIdAndSubscriptionId(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
        verify(mySubscriptionJpaRepository).delete(testMySubscriptionEntity);
    }

    @Test
    @DisplayName("존재하지 않는 구독 취소 시 예외가 발생해야 함")
    void givenNonExistentUserIdAndSubscriptionId_whenDelete_thenThrowException() {
        // Given
        given(mySubscriptionJpaRepository.findByUserIdAndSubscriptionId(TEST_USER_ID, TEST_SUBSCRIPTION_ID))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> subscriptionDataAccess.delete(TEST_USER_ID, TEST_SUBSCRIPTION_ID))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("구독 정보를 찾을 수 없습니다");

        // 메소드 호출 검증
        verify(mySubscriptionJpaRepository).findByUserIdAndSubscriptionId(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("모든 카테고리 조회 시 정확한 도메인 객체로 변환해야 함")
    void whenFindAllCategories_thenReturnDomainObjects() {
        // Given
        List<CategoryEntity> entities = Arrays.asList(
                testCategoryEntity,
                new CategoryEntity("MUSIC", "음악")
        );
        given(categoryJpaRepository.findAll()).willReturn(entities);

        // When
        List<Category> result = subscriptionDataAccess.findAllCategories();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryId()).isEqualTo(TEST_CATEGORY_ID);
        assertThat(result.get(0).getCategoryName()).isEqualTo("OTT/동영상");
        assertThat(result.get(1).getCategoryId()).isEqualTo("MUSIC");
        assertThat(result.get(1).getCategoryName()).isEqualTo("음악");

        // 메소드 호출 검증
        verify(categoryJpaRepository).findAll();
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회 시 정확한 도메인 객체로 변환해야 함")
    void givenCategoryId_whenFindSubscriptionsByCategory_thenReturnDomainObjects() {
        // Given
        List<SubscriptionEntity> entities = Arrays.asList(
                testSubscriptionEntity,
                new SubscriptionEntity(2L, "Disney+", "OTT", "Disney streaming", 9900, 4, "/images/disney.png")
        );
        given(subscriptionJpaRepository.findByCategoryId(TEST_CATEGORY_ID)).willReturn(entities);

        // When
        List<Subscription> result = subscriptionDataAccess.findSubscriptionsByCategory(TEST_CATEGORY_ID);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(TEST_SUBSCRIPTION_ID);
        assertThat(result.get(0).getName()).isEqualTo("Netflix");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("Disney+");

        // 메소드 호출 검증
        verify(subscriptionJpaRepository).findByCategoryId(TEST_CATEGORY_ID);
    }
}