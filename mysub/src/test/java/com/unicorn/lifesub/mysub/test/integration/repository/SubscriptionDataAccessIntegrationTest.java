package com.unicorn.lifesub.mysub.test.integration.repository;

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
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * SubscriptionDataAccess 통합 테스트 클래스입니다.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("integration-test")
public class SubscriptionDataAccessIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13.2-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MySubscriptionJpaRepository mySubscriptionJpaRepository;

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private SubscriptionDataAccess subscriptionDataAccess;

    private static final String USER_ID = "testUser";
    private static final Long SUBSCRIPTION_ID_1 = 1L;
    private static final Long SUBSCRIPTION_ID_2 = 2L;
    private static final String CATEGORY_ID_OTT = "OTT";
    private static final String CATEGORY_ID_MUSIC = "MUSIC";

    @BeforeEach
    void setUp() {
        subscriptionDataAccess = new SubscriptionDataAccess(
                mySubscriptionJpaRepository,
                subscriptionJpaRepository,
                categoryJpaRepository
        );

        // 테스트 데이터 초기화
        mySubscriptionJpaRepository.deleteAll();
        subscriptionJpaRepository.deleteAll();
        categoryJpaRepository.deleteAll();

        // 카테고리 데이터 설정
        CategoryEntity ottCategory = CategoryEntity.builder()
                .categoryId(CATEGORY_ID_OTT)
                .categoryName("OTT/동영상")
                .build();

        CategoryEntity musicCategory = CategoryEntity.builder()
                .categoryId(CATEGORY_ID_MUSIC)
                .categoryName("음악")
                .build();

        categoryJpaRepository.saveAll(Arrays.asList(ottCategory, musicCategory));

        // 구독 서비스 데이터 설정
        SubscriptionEntity netflix = SubscriptionEntity.builder()
                .id(SUBSCRIPTION_ID_1)
                .name("넷플릭스")
                .categoryId(CATEGORY_ID_OTT)
                .description("글로벌 스트리밍 서비스")
                .fee(13900)
                .maxShareNum(4)
                .logoUrl("/images/netflix.png")
                .build();

        SubscriptionEntity spotify = SubscriptionEntity.builder()
                .id(SUBSCRIPTION_ID_2)
                .name("스포티파이")
                .categoryId(CATEGORY_ID_MUSIC)
                .description("음악 스트리밍 서비스")
                .fee(10900)
                .maxShareNum(6)
                .logoUrl("/images/spotify.png")
                .build();

        subscriptionJpaRepository.saveAll(Arrays.asList(netflix, spotify));

        // 사용자 구독 데이터 설정
        MySubscriptionEntity mySubscription = MySubscriptionEntity.builder()
                .userId(USER_ID)
                .subscriptionId(SUBSCRIPTION_ID_1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mySubscriptionJpaRepository.save(mySubscription);
    }

    @Test
    @DisplayName("사용자 ID로 구독 목록 조회")
    void givenUserId_whenFindByUserId_thenReturnSubscriptionList() {
        // When
        List<MySubscription> result = subscriptionDataAccess.findByUserId(USER_ID);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(USER_ID);
        assertThat(result.get(0).getSubscriptionId()).isEqualTo(SUBSCRIPTION_ID_1);
    }

    @Test
    @DisplayName("구독 서비스 ID로 구독 정보 조회")
    void givenSubscriptionId_whenFindSubscriptionById_thenReturnSubscription() {
        // When
        Optional<Subscription> result = subscriptionDataAccess.findSubscriptionById(SUBSCRIPTION_ID_1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(SUBSCRIPTION_ID_1);
        assertThat(result.get().getName()).isEqualTo("넷플릭스");
        assertThat(result.get().getCategoryId()).isEqualTo(CATEGORY_ID_OTT);
        assertThat(result.get().getFee()).isEqualTo(13900);
        assertThat(result.get().getMaxShareNum()).isEqualTo(4);
    }

    @Test
    @DisplayName("존재하지 않는 구독 서비스 ID로 조회 시 빈 Optional 반환")
    void givenNonExistentSubscriptionId_whenFindSubscriptionById_thenReturnEmptyOptional() {
        // When
        Optional<Subscription> result = subscriptionDataAccess.findSubscriptionById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("내 구독 정보 저장")
    void givenMySubscription_whenSave_thenSaveAndReturnMySubscription() {
        // Given
        MySubscription mySubscription = new MySubscription(USER_ID, SUBSCRIPTION_ID_2);

        // When
        MySubscription result = subscriptionDataAccess.save(mySubscription);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getSubscriptionId()).isEqualTo(SUBSCRIPTION_ID_2);

        // DB에 저장되었는지 확인
        Optional<MySubscriptionEntity> savedEntity = mySubscriptionJpaRepository.findByUserIdAndSubscriptionId(USER_ID, SUBSCRIPTION_ID_2);
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getUserId()).isEqualTo(USER_ID);
        assertThat(savedEntity.get().getSubscriptionId()).isEqualTo(SUBSCRIPTION_ID_2);
    }

    @Test
    @DisplayName("구독 취소")
    void givenUserIdAndSubscriptionId_whenDelete_thenDeleteSubscription() {
        // When
        subscriptionDataAccess.delete(USER_ID, SUBSCRIPTION_ID_1);

        // Then
        Optional<MySubscriptionEntity> deletedEntity = mySubscriptionJpaRepository.findByUserIdAndSubscriptionId(USER_ID, SUBSCRIPTION_ID_1);
        assertThat(deletedEntity).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 구독 취소 시 예외 발생")
    void givenNonExistentSubscription_whenDelete_thenThrowException() {
        // When & Then
        assertThatThrownBy(() -> subscriptionDataAccess.delete(USER_ID, 999L))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("구독 정보를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void whenFindAllCategories_thenReturnCategoryList() {
        // When
        List<Category> result = subscriptionDataAccess.findAllCategories();

        // Then
        assertThat(result).hasSize(2);

        Category ottCategory = result.stream()
                .filter(c -> c.getCategoryId().equals(CATEGORY_ID_OTT))
                .findFirst()
                .orElse(null);

        Category musicCategory = result.stream()
                .filter(c -> c.getCategoryId().equals(CATEGORY_ID_MUSIC))
                .findFirst()
                .orElse(null);

        assertThat(ottCategory).isNotNull();
        assertThat(ottCategory.getCategoryName()).isEqualTo("OTT/동영상");

        assertThat(musicCategory).isNotNull();
        assertThat(musicCategory.getCategoryName()).isEqualTo("음악");
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회")
    void givenCategoryId_whenFindSubscriptionsByCategory_thenReturnSubscriptionList() {
        // When
        List<Subscription> result = subscriptionDataAccess.findSubscriptionsByCategory(CATEGORY_ID_OTT);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(SUBSCRIPTION_ID_1);
        assertThat(result.get(0).getName()).isEqualTo("넷플릭스");
        assertThat(result.get(0).getCategoryId()).isEqualTo(CATEGORY_ID_OTT);
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 구독 서비스 목록 조회 시 빈 목록 반환")
    void givenNonExistentCategoryId_whenFindSubscriptionsByCategory_thenReturnEmptyList() {
        // When
        List<Subscription> result = subscriptionDataAccess.findSubscriptionsByCategory("NON_EXISTENT");

        // Then
        assertThat(result).isEmpty();
    }
}