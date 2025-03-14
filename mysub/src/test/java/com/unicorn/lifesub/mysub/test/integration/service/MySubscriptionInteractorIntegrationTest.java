package com.unicorn.lifesub.mysub.test.integration.service;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.mysub.biz.domain.Category;
import com.unicorn.lifesub.mysub.biz.domain.MySubscription;
import com.unicorn.lifesub.mysub.biz.domain.Subscription;
import com.unicorn.lifesub.mysub.biz.service.MySubscriptionInteractor;
import com.unicorn.lifesub.mysub.biz.usecase.out.SubscriptionDataAccessInterface;
import com.unicorn.lifesub.mysub.infra.dto.*;
import com.unicorn.lifesub.mysub.test.integration.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * MySubscriptionInteractor 통합 테스트 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("integration-test")
public class MySubscriptionInteractorIntegrationTest {

    @Mock
    private SubscriptionDataAccessInterface subscriptionDataAccess;

    @InjectMocks
    private MySubscriptionInteractor mySubscriptionInteractor;

    @Test
    @DisplayName("사용자의 총 구독료 조회 - 정상 케이스")
    void givenUserWithSubscriptions_whenGetTotalFee_thenReturnCorrectFee() {
        // Given
        String userId = "user01";
        List<MySubscription> mySubscriptions = Arrays.asList(
                new MySubscription(userId, 1L),
                new MySubscription(userId, 2L)
        );

        Subscription sub1 = new Subscription(1L, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png");
        Subscription sub2 = new Subscription(2L, "유튜브 프리미엄", "OTT", "동영상 스트리밍 서비스", 14900, 5, "/images/youtube.png");

        when(subscriptionDataAccess.findByUserId(userId)).thenReturn(mySubscriptions);
        when(subscriptionDataAccess.findSubscriptionById(1L)).thenReturn(Optional.of(sub1));
        when(subscriptionDataAccess.findSubscriptionById(2L)).thenReturn(Optional.of(sub2));

        // When
        TotalFeeResponse result = mySubscriptionInteractor.getTotalFee(userId);

        // Then
        assertThat(result.getTotalFee()).isEqualTo(13900 + 14900);
        // 실제 코드의 구현에 맞게 이미지 경로 기대값 수정
        assertThat(result.getRangeImage()).isEqualTo("/images/sub_lover.png"); // 10만원 미만

        verify(subscriptionDataAccess).findByUserId(userId);
        verify(subscriptionDataAccess).findSubscriptionById(1L);
        verify(subscriptionDataAccess).findSubscriptionById(2L);
    }

    @Test
    @DisplayName("사용자의 총 구독료 조회 - 구독 없는 경우")
    void givenUserWithNoSubscriptions_whenGetTotalFee_thenReturnZeroFee() {
        // Given
        String userId = "user01";
        when(subscriptionDataAccess.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        TotalFeeResponse result = mySubscriptionInteractor.getTotalFee(userId);

        // Then
        assertThat(result.getTotalFee()).isEqualTo(0);
        assertThat(result.getRangeImage()).isEqualTo("/images/sub_lover.png"); // 10만원 미만

        verify(subscriptionDataAccess).findByUserId(userId);
        verifyNoMoreInteractions(subscriptionDataAccess);
    }

    @Test
    @DisplayName("사용자의 구독 목록 조회")
    void givenUserWithSubscriptions_whenGetMySubscriptions_thenReturnSubscriptionList() {
        // Given
        String userId = "user01";
        List<MySubscription> mySubscriptions = Arrays.asList(
                new MySubscription(userId, 1L),
                new MySubscription(userId, 2L)
        );

        Subscription sub1 = new Subscription(1L, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png");
        Subscription sub2 = new Subscription(2L, "유튜브 프리미엄", "OTT", "동영상 스트리밍 서비스", 14900, 5, "/images/youtube.png");

        when(subscriptionDataAccess.findByUserId(userId)).thenReturn(mySubscriptions);
        when(subscriptionDataAccess.findSubscriptionById(1L)).thenReturn(Optional.of(sub1));
        when(subscriptionDataAccess.findSubscriptionById(2L)).thenReturn(Optional.of(sub2));

        // When
        List<MySubscriptionListResponse> result = mySubscriptionInteractor.getMySubscriptions(userId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSubscriptionId()).isEqualTo(1L);
        assertThat(result.get(0).getServiceName()).isEqualTo("넷플릭스");
        assertThat(result.get(0).getLogoUrl()).isEqualTo("/images/netflix.png");
        assertThat(result.get(1).getSubscriptionId()).isEqualTo(2L);
        assertThat(result.get(1).getServiceName()).isEqualTo("유튜브 프리미엄");

        verify(subscriptionDataAccess).findByUserId(userId);
        verify(subscriptionDataAccess).findSubscriptionById(1L);
        verify(subscriptionDataAccess).findSubscriptionById(2L);
    }

    @Test
    @DisplayName("구독 서비스 상세 정보 조회")
    void givenValidSubscriptionId_whenGetSubscriptionDetail_thenReturnDetailInfo() {
        // Given
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription(
                subscriptionId, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png"
        );

        when(subscriptionDataAccess.findSubscriptionById(subscriptionId)).thenReturn(Optional.of(subscription));

        // When
        SubscriptionDetailResponse result = mySubscriptionInteractor.getSubscriptionDetail(subscriptionId);

        // Then
        assertThat(result.getSubscriptionId()).isEqualTo(subscriptionId);
        assertThat(result.getServiceName()).isEqualTo("넷플릭스");
        assertThat(result.getCategory()).isEqualTo("OTT");
        assertThat(result.getDescription()).isEqualTo("글로벌 스트리밍 서비스");
        assertThat(result.getFee()).isEqualTo(13900);
        assertThat(result.getMaxShareNum()).isEqualTo(4);

        verify(subscriptionDataAccess).findSubscriptionById(subscriptionId);
    }

    @Test
    @DisplayName("존재하지 않는 구독 서비스 조회 시 예외 발생")
    void givenInvalidSubscriptionId_whenGetSubscriptionDetail_thenThrowException() {
        // Given
        Long subscriptionId = 999L;
        when(subscriptionDataAccess.findSubscriptionById(subscriptionId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> mySubscriptionInteractor.getSubscriptionDetail(subscriptionId))
                .isInstanceOf(BizException.class)
                .hasMessageContaining("찾을 수 없습니다");

        verify(subscriptionDataAccess).findSubscriptionById(subscriptionId);
    }

    @Test
    @DisplayName("구독 서비스 구독 성공")
    void givenValidUserIdAndSubscriptionId_whenSubscribe_thenReturnSuccess() {
        // Given
        String userId = "user01";
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription(
                subscriptionId, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png"
        );

        when(subscriptionDataAccess.findSubscriptionById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(subscriptionDataAccess.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(subscriptionDataAccess.save(any(MySubscription.class))).thenReturn(new MySubscription(userId, subscriptionId));

        // When
        SubscribeResponse result = mySubscriptionInteractor.subscribeSub(userId, subscriptionId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("구독이 완료되었습니다.");

        verify(subscriptionDataAccess).findSubscriptionById(subscriptionId);
        verify(subscriptionDataAccess).findByUserId(userId);
        verify(subscriptionDataAccess).save(any(MySubscription.class));
    }

    @Test
    @DisplayName("이미 구독 중인 서비스 구독 시도")
    void givenAlreadySubscribedService_whenSubscribe_thenReturnFailure() {
        // Given
        String userId = "user01";
        Long subscriptionId = 1L;
        Subscription subscription = new Subscription(
                subscriptionId, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png"
        );
        List<MySubscription> existingSubscriptions = Collections.singletonList(new MySubscription(userId, subscriptionId));

        when(subscriptionDataAccess.findSubscriptionById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(subscriptionDataAccess.findByUserId(userId)).thenReturn(existingSubscriptions);

        // When
        SubscribeResponse result = mySubscriptionInteractor.subscribeSub(userId, subscriptionId);

        // Then
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getMessage()).isEqualTo("이미 구독 중인 서비스입니다.");

        verify(subscriptionDataAccess).findSubscriptionById(subscriptionId);
        verify(subscriptionDataAccess).findByUserId(userId);
        verifyNoMoreInteractions(subscriptionDataAccess);
    }

    @Test
    @DisplayName("구독 서비스 취소 성공")
    void givenValidUserIdAndSubscriptionId_whenCancelSub_thenReturnSuccess() {
        // Given
        String userId = "user01";
        Long subscriptionId = 1L;

        doNothing().when(subscriptionDataAccess).delete(userId, subscriptionId);

        // When
        SuccessResponse result = mySubscriptionInteractor.cancelSub(userId, subscriptionId);

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo("구독이 취소되었습니다.");

        verify(subscriptionDataAccess).delete(userId, subscriptionId);
    }

    @Test
    @DisplayName("구독 카테고리 목록 조회")
    void whenGetCategories_thenReturnCategoryList() {
        // Given
        List<Category> categories = Arrays.asList(
                Category.builder().categoryId("OTT").categoryName("OTT/동영상").build(),
                Category.builder().categoryId("MUSIC").categoryName("음악").build(),
                Category.builder().categoryId("FOOD").categoryName("식품").build()
        );

        when(subscriptionDataAccess.findAllCategories()).thenReturn(categories);

        // When
        List<CategoryListDTO> result = mySubscriptionInteractor.getCategories();

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getCategoryName()).isEqualTo("OTT/동영상");
        assertThat(result.get(1).getCategoryName()).isEqualTo("음악");
        assertThat(result.get(2).getCategoryName()).isEqualTo("식품");

        verify(subscriptionDataAccess).findAllCategories();
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회")
    void givenValidCategoryId_whenGetSubscriptionsByCategory_thenReturnSubscriptionList() {
        // Given
        String categoryId = "OTT";
        List<Subscription> subscriptions = Arrays.asList(
                new Subscription(1L, "넷플릭스", categoryId, "글로벌 스트리밍 서비스", 13900, 4, "/images/netflix.png"),
                new Subscription(2L, "왓챠", categoryId, "국내 스트리밍 서비스", 9900, 4, "/images/watcha.png")
        );

        when(subscriptionDataAccess.findSubscriptionsByCategory(categoryId)).thenReturn(subscriptions);

        // When
        List<SubscriptionListResponse> result = mySubscriptionInteractor.getSubscriptionsByCategory(categoryId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getSubscriptionId()).isEqualTo(1L);
        assertThat(result.get(0).getServiceName()).isEqualTo("넷플릭스");
        assertThat(result.get(0).getDescription()).isEqualTo("글로벌 스트리밍 서비스");
        assertThat(result.get(0).getFee()).isEqualTo(13900);
        assertThat(result.get(1).getSubscriptionId()).isEqualTo(2L);
        assertThat(result.get(1).getServiceName()).isEqualTo("왓챠");

        verify(subscriptionDataAccess).findSubscriptionsByCategory(categoryId);
    }
}