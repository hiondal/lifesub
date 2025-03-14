package com.unicorn.lifesub.mysub.test.unit.infra.controller;

import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.infra.controller.MySubscriptionController;
import com.unicorn.lifesub.mysub.infra.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * MySubscriptionController 단위 테스트 클래스입니다.
 */
@ExtendWith(MockitoExtension.class)
class MySubscriptionControllerUnitTest {

    @Mock
    private TotalFeeInputBoundary totalFeeInputBoundary;

    @Mock
    private MySubscriptionsInputBoundary mySubscriptionsInputBoundary;

    @Mock
    private SubscriptionDetailInputBoundary subscriptionDetailInputBoundary;

    @Mock
    private SubscribeInputBoundary subscribeInputBoundary;

    @Mock
    private CancelSubscriptionInputBoundary cancelSubscriptionInputBoundary;

    @Mock
    private SubscriptionCategoriesInputBoundary subscriptionCategoriesInputBoundary;

    @Mock
    private SubscriptionsByCategoryInputBoundary subscriptionsByCategoryInputBoundary;

    @InjectMocks
    private MySubscriptionController mySubscriptionController;

    private static final String TEST_USER_ID = "user01";
    private static final Long TEST_SUBSCRIPTION_ID = 1L;
    private static final String TEST_CATEGORY_ID = "OTT";

    @Test
    @DisplayName("사용자 총 구독료 조회 API가 적절한 응답을 반환해야 함")
    void givenUserId_whenGetTotalFee_thenReturnTotalFeeResponse() {
        // Given
        TotalFeeResponse expectedResponse = new TotalFeeResponse(13900, "/images/sub_lover.png");
        given(totalFeeInputBoundary.getTotalFee(TEST_USER_ID)).willReturn(expectedResponse);

        // When
        ResponseEntity<TotalFeeResponse> response = mySubscriptionController.getTotalFee(TEST_USER_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(totalFeeInputBoundary).getTotalFee(TEST_USER_ID);
    }

    @Test
    @DisplayName("사용자 구독 목록 조회 API가 적절한 응답을 반환해야 함")
    void givenUserId_whenGetMySubscriptions_thenReturnMySubscriptionListResponses() {
        // Given
        List<MySubscriptionListResponse> expectedResponses = Arrays.asList(
                new MySubscriptionListResponse(1L, "Netflix", "/images/netflix.png"),
                new MySubscriptionListResponse(2L, "Disney+", "/images/disney.png")
        );
        given(mySubscriptionsInputBoundary.getMySubscriptions(TEST_USER_ID)).willReturn(expectedResponses);

        // When
        ResponseEntity<List<MySubscriptionListResponse>> response = mySubscriptionController.getMySubscriptions(TEST_USER_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponses);
        verify(mySubscriptionsInputBoundary).getMySubscriptions(TEST_USER_ID);
    }

    @Test
    @DisplayName("구독 서비스 상세 정보 조회 API가 적절한 응답을 반환해야 함")
    void givenSubscriptionId_whenGetSubscriptionDetail_thenReturnSubscriptionDetailResponse() {
        // Given
        SubscriptionDetailResponse expectedResponse = new SubscriptionDetailResponse(
                TEST_SUBSCRIPTION_ID,
                "Netflix",
                "OTT",
                "Streaming service",
                13900,
                4
        );
        given(subscriptionDetailInputBoundary.getSubscriptionDetail(TEST_SUBSCRIPTION_ID)).willReturn(expectedResponse);

        // When
        ResponseEntity<SubscriptionDetailResponse> response = mySubscriptionController.getSubscriptionDetail(TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(subscriptionDetailInputBoundary).getSubscriptionDetail(TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 신청 API가 적절한 응답을 반환해야 함")
    void givenUserIdAndSubscriptionId_whenSubscribeSub_thenReturnSubscribeResponse() {
        // Given
        SubscribeResponse expectedResponse = new SubscribeResponse(true, "구독이 완료되었습니다.");
        given(subscribeInputBoundary.subscribeSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID)).willReturn(expectedResponse);

        // When
        ResponseEntity<SubscribeResponse> response = mySubscriptionController.subscribeSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(subscribeInputBoundary).subscribeSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 취소 API가 적절한 응답을 반환해야 함")
    void givenUserIdAndSubscriptionId_whenCancelSub_thenReturnSuccessResponse() {
        // Given
        SuccessResponse expectedResponse = new SuccessResponse(true, "구독이 취소되었습니다.");
        given(cancelSubscriptionInputBoundary.cancelSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID)).willReturn(expectedResponse);

        // When
        ResponseEntity<SuccessResponse> response = mySubscriptionController.cancelSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(cancelSubscriptionInputBoundary).cancelSub(TEST_USER_ID, TEST_SUBSCRIPTION_ID);
    }

    @Test
    @DisplayName("구독 카테고리 목록 조회 API가 적절한 응답을 반환해야 함")
    void whenGetSubscriptionCategories_thenReturnCategoryListDTOs() {
        // Given
        List<CategoryListDTO> expectedResponses = Arrays.asList(
                new CategoryListDTO("OTT/동영상"),
                new CategoryListDTO("음악")
        );
        given(subscriptionCategoriesInputBoundary.getCategories()).willReturn(expectedResponses);

        // When
        ResponseEntity<List<CategoryListDTO>> response = mySubscriptionController.getSubscriptionCategories();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponses);
        verify(subscriptionCategoriesInputBoundary).getCategories();
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회 API가 적절한 응답을 반환해야 함")
    void givenCategoryId_whenGetSubscriptionsByCategory_thenReturnSubscriptionListResponses() {
        // Given
        List<SubscriptionListResponse> expectedResponses = Arrays.asList(
                new SubscriptionListResponse(1L, "Netflix", "/images/netflix.png", "Streaming service", 13900),
                new SubscriptionListResponse(2L, "Disney+", "/images/disney.png", "Disney streaming", 9900)
        );
        given(subscriptionsByCategoryInputBoundary.getSubscriptionsByCategory(TEST_CATEGORY_ID)).willReturn(expectedResponses);

        // When
        ResponseEntity<List<SubscriptionListResponse>> response = mySubscriptionController.getSubscriptionsByCategory(TEST_CATEGORY_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponses);
        verify(subscriptionsByCategoryInputBoundary).getSubscriptionsByCategory(TEST_CATEGORY_ID);
    }
}