package com.unicorn.lifesub.mysub.test.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.lifesub.mysub.infra.dto.*;
import com.unicorn.lifesub.mysub.infra.gateway.entity.SubscriptionEntity;
import com.unicorn.lifesub.mysub.infra.gateway.repository.MySubscriptionJpaRepository;
import com.unicorn.lifesub.mysub.test.e2e.config.TestContainerConfig;
import com.unicorn.lifesub.mysub.test.e2e.support.TestDataManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 마이구독 서비스에 대한 E2E 테스트 클래스입니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("e2e-test")
@Transactional
class MySubscriptionE2ETest extends TestContainerConfig {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataManager testDataManager;

    @Autowired
    private MySubscriptionJpaRepository mySubscriptionRepository;

    private WebTestClient webClient;

    @Value("${test.user.id}")
    private String TEST_USER_ID;

    @BeforeEach
    void setUp() {
        // WebTestClient 설정
        webClient = MockMvcWebTestClient
                .bindToApplicationContext(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .configureClient()
                .build();

        // 테스트 데이터 초기화
        mySubscriptionRepository.deleteAll();
        testDataManager.setupTestData();
    }

    @Test
    @DisplayName("사용자의 총 구독료를 올바르게 조회할 수 있어야 한다")
    void givenUserWithSubscriptions_whenGetTotalFee_thenReturnCorrectAmount() {
        // Given
        int expectedTotalFee = testDataManager.calculateTotalFee();
        String authToken = testDataManager.getTestUserToken();

        // When & Then
        webClient.get()
                .uri("/api/users/{userId}/subscriptions/total-fee", TEST_USER_ID)
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TotalFeeResponse.class)
                .value(response -> {
                    assertThat(response.getTotalFee()).isEqualTo(expectedTotalFee);
                    assertThat(response.getRangeImage()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("사용자의 구독 목록을 올바르게 조회할 수 있어야 한다")
    void givenUserWithSubscriptions_whenGetMySubscriptions_thenReturnCorrectList() {
        // Given
        List<Long> subscribedIds = testDataManager.getUserSubscriptionIds(TEST_USER_ID);
        String authToken = testDataManager.getTestUserToken();

        // When & Then
        webClient.get()
                .uri("/api/users/{userId}/subscriptions", TEST_USER_ID)
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MySubscriptionListResponse.class)
                .value(responseList -> {
                    assertThat(responseList).hasSize(subscribedIds.size());
                    assertThat(responseList.stream().map(MySubscriptionListResponse::getSubscriptionId).toList())
                            .containsExactlyInAnyOrderElementsOf(subscribedIds);
                });
    }

    @Test
    @DisplayName("구독 서비스 상세 정보를 올바르게 조회할 수 있어야 한다")
    void givenExistingSubscription_whenGetSubscriptionDetail_thenReturnCorrectDetails() {
        // Given
        SubscriptionEntity subscription = testDataManager.getTestSubscription();
        String authToken = testDataManager.getTestUserToken();

        // When & Then
        webClient.get()
                .uri("/api/subscriptions/{subscriptionId}", subscription.getId())
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubscriptionDetailResponse.class)
                .value(response -> {
                    assertThat(response.getSubscriptionId()).isEqualTo(subscription.getId());
                    assertThat(response.getServiceName()).isEqualTo(subscription.getName());
                    assertThat(response.getCategory()).isEqualTo(subscription.getCategoryId());
                    assertThat(response.getDescription()).isEqualTo(subscription.getDescription());
                    assertThat(response.getFee()).isEqualTo(subscription.getFee());
                    assertThat(response.getMaxShareNum()).isEqualTo(subscription.getMaxShareNum());
                });
    }

    @Test
    @DisplayName("새 구독 서비스를 구독할 수 있어야 한다")
    void givenUnsubscribedService_whenSubscribe_thenSuccessfullySubscribed() {
        // Given
        Long unsubscribedServiceId = testDataManager.getUnsubscribedServiceId(TEST_USER_ID);
        String authToken = testDataManager.getTestUserToken();
        assertThat(unsubscribedServiceId).isNotNull();

        // When & Then
        webClient.post()
                .uri("/api/users/{userId}/subscriptions?subscriptionId={subscriptionId}",
                        TEST_USER_ID, unsubscribedServiceId)
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SubscribeResponse.class)
                .value(response -> {
                    assertThat(response.isSuccess()).isTrue();
                    assertThat(response.getMessage()).contains("완료");
                });

        // 구독 목록이 업데이트 되었는지 확인
        List<Long> updatedSubscribedIds = testDataManager.getUserSubscriptionIds(TEST_USER_ID);
        assertThat(updatedSubscribedIds).contains(unsubscribedServiceId);
    }

    @Test
    @DisplayName("구독 중인 서비스를 취소할 수 있어야 한다")
    void givenSubscribedService_whenCancel_thenSuccessfullyCancelled() {
        // Given
        Long subscribedServiceId = testDataManager.getSubscribedServiceId(TEST_USER_ID);
        String authToken = testDataManager.getTestUserToken();
        assertThat(subscribedServiceId).isNotNull();

        // When & Then
        webClient.delete()
                .uri("/api/users/{userId}/subscriptions/{subscriptionId}",
                        TEST_USER_ID, subscribedServiceId)
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SuccessResponse.class)
                .value(response -> {
                    assertThat(response.isSuccess()).isTrue();
                    assertThat(response.getMessage()).contains("취소");
                });

        // 구독 목록에서 해당 서비스가 제거되었는지 확인
        List<Long> updatedSubscribedIds = testDataManager.getUserSubscriptionIds(TEST_USER_ID);
        assertThat(updatedSubscribedIds).doesNotContain(subscribedServiceId);
    }

    @Test
    @DisplayName("구독 카테고리 목록을 올바르게 조회할 수 있어야 한다")
    void whenGetSubscriptionCategories_thenReturnCorrectCategories() {
        // Given
        int expectedCategoryCount = testDataManager.getCategoryCount();
        String authToken = testDataManager.getTestUserToken();

        // When & Then
        webClient.get()
                .uri("/api/subscriptions/categories")
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryListDTO.class)
                .value(categories -> {
                    assertThat(categories).hasSize(expectedCategoryCount);
                    // 카테고리 이름 확인
                    assertThat(categories).extracting(CategoryListDTO::getCategoryName)
                            .contains("OTT/동영상", "음악");
                });
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록을 올바르게 조회할 수 있어야 한다")
    void givenCategoryId_whenGetSubscriptionsByCategory_thenReturnCorrectList() {
        // Given
        String categoryId = "OTT";
        String authToken = testDataManager.getTestUserToken();
        int expectedCount = testDataManager.getSubscriptionCountByCategory(categoryId);

        // When & Then
        webClient.get()
                .uri("/api/subscriptions?categoryId={categoryId}", categoryId)
                .header("Authorization", "Bearer " + authToken)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SubscriptionListResponse.class)
                .value(subscriptions -> {
                    assertThat(subscriptions).hasSize(expectedCount);
                    subscriptions.forEach(subscription -> {
                        assertThat(subscription.getSubscriptionId()).isNotNull();
                        assertThat(subscription.getServiceName()).isNotEmpty();
                        assertThat(subscription.getLogoUrl()).isNotEmpty();
                        assertThat(subscription.getDescription()).isNotEmpty();
                        assertThat(subscription.getFee()).isPositive();
                    });
                });
    }
}