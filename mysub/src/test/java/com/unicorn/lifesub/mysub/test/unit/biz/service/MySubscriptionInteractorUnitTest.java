package com.unicorn.lifesub.mysub.test.unit.biz.service;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.service.MySubscriptionInteractor;
import com.unicorn.lifesub.mysub.biz.usecase.out.SubscriptionDataAccessInterface;
import com.unicorn.lifesub.mysub.infra.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
 * MySubscriptionInteractor 단위 테스트 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
class MySubscriptionInteractorUnitTest {

    @Mock
    private SubscriptionDataAccessInterface subscriptionDataAccess;

    @InjectMocks
    private MySubscriptionInteractor mySubscriptionInteractor;

    private static final String TEST_USER_ID = "user01";
    private static final Long TEST_SUBSCRIPTION_ID = 1L;
    private static final String TEST_CATEGORY_ID = "OTT";

    private Subscription testSubscription;
    private MySubscription testMySubscription;
    private List<MySubscription> testMySubscriptions;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        testSubscription = new Subscription(
                TEST_SUBSCRIPTION_ID,
                "Netflix",
                "OTT",
                "Streaming service",
                13900,
                4,
                "/images/netflix.png"
        );

        testMySubscription = new MySubscription(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
        testMySubscriptions = Collections.singletonList(testMySubscription);

        testCategory = Category.builder()
                .categoryId(TEST_CATEGORY_ID)
                .categoryName("OTT/동영상")
                .build();
    }

    @Test
    @DisplayName("총 구독료 조회 시 정확한 금액과 범위 이미지를 반환해야 함")
    void givenUserWithSubscriptions_whenGetTotalFee_thenReturnCorrectFeeAndImage() {
        // Given
        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(testMySubscriptions);
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscription));

        // When
        TotalFeeResponse response = mySubscriptionInteractor.getTotalFee(TEST_USER_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalFee()).isEqualTo(13900);
        assertThat(response.getRangeImage()).isEqualTo("/images/sub_lover.png");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findByUserId(TEST_USER_ID);
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 목록 조회 시 정확한 정보를 반환해야 함")
    void givenUserWithSubscriptions_whenGetMySubscriptions_thenReturnSubscriptionList() {
        // Given
        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(testMySubscriptions);
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscription));

        // When
        List<MySubscriptionListResponse> responses = mySubscriptionInteractor.getMySubscriptions(TEST_USER_ID);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getSubscriptionId()).isEqualTo(TEST_SUBSCRIPTION_ID);
        assertThat(responses.get(0).getServiceName()).isEqualTo("Netflix");
        assertThat(responses.get(0).getLogoUrl()).isEqualTo("/images/netflix.png");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findByUserId(TEST_USER_ID);
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 상세 정보 조회 시 정확한 정보를 반환해야 함")
    void givenSubscriptionId_whenGetSubscriptionDetail_thenReturnDetailInfo() {
        // Given
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscription));

        // When
        SubscriptionDetailResponse response = mySubscriptionInteractor.getSubscriptionDetail(TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getSubscriptionId()).isEqualTo(TEST_SUBSCRIPTION_ID);
        assertThat(response.getServiceName()).isEqualTo("Netflix");
        assertThat(response.getCategory()).isEqualTo("OTT");
        assertThat(response.getDescription()).isEqualTo("Streaming service");
        assertThat(response.getFee()).isEqualTo(13900);
        assertThat(response.getMaxShareNum()).isEqualTo(4);

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("존재하지 않는 구독 상세 정보 조회 시 예외가 발생해야 함")
    void givenNonExistentSubscriptionId_whenGetSubscriptionDetail_thenThrowException() {
        // Given
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> mySubscriptionInteractor.getSubscriptionDetail(TEST_SUBSCRIPTION_ID))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("구독 서비스 정보를 찾을 수 없습니다");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 신청 시 성공 응답을 반환해야 함")
    void givenUserAndSubscriptionId_whenSubscribeSub_thenReturnSuccessResponse() {
        // Given
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscription));
        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(Collections.emptyList());
        given(subscriptionDataAccess.save(any(MySubscription.class))).willReturn(testMySubscription);

        // When
        SubscribeResponse response = mySubscriptionInteractor.subscribeSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("구독이 완료되었습니다.");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
        verify(subscriptionDataAccess).findByUserId(TEST_USER_ID);
        verify(subscriptionDataAccess).save(any(MySubscription.class));
    }

    @Test
    @DisplayName("이미 구독 중인 서비스의 구독 신청 시 실패 응답을 반환해야 함")
    void givenUserWithExistingSubscription_whenSubscribeSub_thenReturnFailureResponse() {
        // Given
        given(subscriptionDataAccess.findSubscriptionById(TEST_SUBSCRIPTION_ID)).willReturn(Optional.of(testSubscription));
        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(testMySubscriptions);

        // When
        SubscribeResponse response = mySubscriptionInteractor.subscribeSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("이미 구독 중인 서비스입니다.");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findSubscriptionById(TEST_SUBSCRIPTION_ID);
        verify(subscriptionDataAccess).findByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("구독 취소 시 성공 응답을 반환해야 함")
    void givenUserAndSubscriptionId_whenCancelSub_thenReturnSuccessResponse() {
        // When
        SuccessResponse response = mySubscriptionInteractor.cancelSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("구독이 취소되었습니다.");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).delete(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("카테고리 목록 조회 시 정확한 정보를 반환해야 함")
    void givenCategories_whenGetCategories_thenReturnCategoryList() {
        // Given
        List<Category> categories = Arrays.asList(
                Category.builder().categoryId("OTT").categoryName("OTT/동영상").build(),
                Category.builder().categoryId("MUSIC").categoryName("음악").build()
        );
        given(subscriptionDataAccess.findAllCategories()).willReturn(categories);

        // When
        List<CategoryListDTO> responses = mySubscriptionInteractor.getCategories();

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getCategoryName()).isEqualTo("OTT/동영상");
        assertThat(responses.get(1).getCategoryName()).isEqualTo("음악");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findAllCategories();
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회 시 정확한 정보를 반환해야 함")
    void givenCategoryId_whenGetSubscriptionsByCategory_thenReturnSubscriptionList() {
        // Given
        List<Subscription> subscriptions = Arrays.asList(
                new Subscription(1L, "Netflix", "OTT", "Streaming service", 13900, 4, "/images/netflix.png"),
                new Subscription(2L, "Disney+", "OTT", "Disney streaming", 9900, 4, "/images/disney.png")
        );
        given(subscriptionDataAccess.findSubscriptionsByCategory(TEST_CATEGORY_ID)).willReturn(subscriptions);

        // When
        List<SubscriptionListResponse> responses = mySubscriptionInteractor.getSubscriptionsByCategory(TEST_CATEGORY_ID);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSubscriptionId()).isEqualTo(1L);
        assertThat(responses.get(0).getServiceName()).isEqualTo("Netflix");
        assertThat(responses.get(1).getSubscriptionId()).isEqualTo(2L);
        assertThat(responses.get(1).getServiceName()).isEqualTo("Disney+");

        // 메소드 호출 검증
        verify(subscriptionDataAccess).findSubscriptionsByCategory(TEST_CATEGORY_ID);
    }

    @Test
    @DisplayName("총 구독료가 10만원 이상 20만원 이하인 경우 적절한 이미지를 반환해야 함")
    void givenUserWithMediumSubscriptionFee_whenGetTotalFee_thenReturnMediumRangeImage() {
        // Given
        Subscription highFeeSubscription = new Subscription(
                2L,
                "Premium Package",
                "BUNDLE",
                "Premium bundle",
                110000,
                1,
                "/images/premium.png"
        );

        MySubscription highFeeMySubscription = new MySubscription(TEST_USER_ID, 2L);

        List<MySubscription> subscriptions = Collections.singletonList(highFeeMySubscription);

        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(subscriptions);
        given(subscriptionDataAccess.findSubscriptionById(2L)).willReturn(Optional.of(highFeeSubscription));

        // When
        TotalFeeResponse response = mySubscriptionInteractor.getTotalFee(TEST_USER_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalFee()).isEqualTo(110000);
        assertThat(response.getRangeImage()).isEqualTo("/images/sub_collector.png");
    }

    @Test
    @DisplayName("총 구독료가 20만원 초과인 경우 적절한 이미지를 반환해야 함")
    void givenUserWithHighSubscriptionFee_whenGetTotalFee_thenReturnHighRangeImage() {
        // Given
        MySubscription sub1 = new MySubscription(TEST_USER_ID, 1L);
        MySubscription sub2 = new MySubscription(TEST_USER_ID, 2L);
        MySubscription sub3 = new MySubscription(TEST_USER_ID, 3L);

        Subscription highFeeSubscription1 = new Subscription(1L, "Premium1", "BUNDLE", "Premium1", 80000, 1, "/images/premium1.png");
        Subscription highFeeSubscription2 = new Subscription(2L, "Premium2", "BUNDLE", "Premium2", 80000, 1, "/images/premium2.png");
        Subscription highFeeSubscription3 = new Subscription(3L, "Premium3", "BUNDLE", "Premium3", 80000, 1, "/images/premium3.png");

        List<MySubscription> subscriptions = Arrays.asList(sub1, sub2, sub3);

        given(subscriptionDataAccess.findByUserId(TEST_USER_ID)).willReturn(subscriptions);
        given(subscriptionDataAccess.findSubscriptionById(1L)).willReturn(Optional.of(highFeeSubscription1));
        given(subscriptionDataAccess.findSubscriptionById(2L)).willReturn(Optional.of(highFeeSubscription2));
        given(subscriptionDataAccess.findSubscriptionById(3L)).willReturn(Optional.of(highFeeSubscription3));

        // When
        TotalFeeResponse response = mySubscriptionInteractor.getTotalFee(TEST_USER_ID);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTotalFee()).isEqualTo(240000);
        assertThat(response.getRangeImage()).isEqualTo("/images/sub_luxury.png");
    }
}