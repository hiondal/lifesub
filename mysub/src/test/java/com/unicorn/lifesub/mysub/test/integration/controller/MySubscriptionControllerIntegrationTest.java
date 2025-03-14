package com.unicorn.lifesub.mysub.test.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.infra.controller.MySubscriptionController;
import com.unicorn.lifesub.mysub.infra.dto.*;
import com.unicorn.lifesub.mysub.test.integration.config.TestConfig;
import com.unicorn.lifesub.mysub.test.integration.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MySubscriptionController 통합 테스트 클래스입니다.
 */
@WebMvcTest(MySubscriptionController.class)
@Import({TestConfig.class, TestSecurityConfig.class})
@ActiveProfiles("integration-test")
public class MySubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TotalFeeInputBoundary totalFeeInputBoundary;

    @Autowired
    private MySubscriptionsInputBoundary mySubscriptionsInputBoundary;

    @Autowired
    private SubscriptionDetailInputBoundary subscriptionDetailInputBoundary;

    @Autowired
    private SubscribeInputBoundary subscribeInputBoundary;

    @Autowired
    private CancelSubscriptionInputBoundary cancelSubscriptionInputBoundary;

    @Autowired
    private SubscriptionCategoriesInputBoundary subscriptionCategoriesInputBoundary;

    @Autowired
    private SubscriptionsByCategoryInputBoundary subscriptionsByCategoryInputBoundary;

    @Test
    @DisplayName("사용자의 총 구독료 조회")
    void givenValidUserId_whenGetTotalFee_thenReturnTotalFee() throws Exception {
        // Given
        String userId = "user01";
        TotalFeeResponse totalFeeResponse = new TotalFeeResponse(45000, "/images/sub_collector.png");

        when(totalFeeInputBoundary.getTotalFee(userId)).thenReturn(totalFeeResponse);

        // When & Then
        mockMvc.perform(get("/api/users/{userId}/subscriptions/total-fee", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(45000))
                .andExpect(jsonPath("$.rangeImage").value("/images/sub_collector.png"));
    }

    @Test
    @DisplayName("사용자의 구독 목록 조회")
    void givenValidUserId_whenGetMySubscriptions_thenReturnSubscriptionList() throws Exception {
        // Given
        String userId = "user01";
        List<MySubscriptionListResponse> subscriptions = Arrays.asList(
                new MySubscriptionListResponse(1L, "넷플릭스", "/images/netflix.png"),
                new MySubscriptionListResponse(2L, "유튜브 프리미엄", "/images/youtube.png")
        );

        when(mySubscriptionsInputBoundary.getMySubscriptions(userId)).thenReturn(subscriptions);

        // When & Then
        mockMvc.perform(get("/api/users/{userId}/subscriptions", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subscriptionId").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("넷플릭스"))
                .andExpect(jsonPath("$[1].subscriptionId").value(2))
                .andExpect(jsonPath("$[1].serviceName").value("유튜브 프리미엄"));
    }

    @Test
    @DisplayName("구독 서비스 상세 정보 조회")
    void givenValidSubscriptionId_whenGetSubscriptionDetail_thenReturnDetailInfo() throws Exception {
        // Given
        Long subscriptionId = 1L;
        SubscriptionDetailResponse detailResponse = new SubscriptionDetailResponse(
                subscriptionId, "넷플릭스", "OTT", "글로벌 스트리밍 서비스", 13900, 4
        );

        when(subscriptionDetailInputBoundary.getSubscriptionDetail(subscriptionId)).thenReturn(detailResponse);

        // When & Then
        mockMvc.perform(get("/api/subscriptions/{subscriptionId}", subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionId").value(subscriptionId))
                .andExpect(jsonPath("$.serviceName").value("넷플릭스"))
                .andExpect(jsonPath("$.category").value("OTT"))
                .andExpect(jsonPath("$.fee").value(13900))
                .andExpect(jsonPath("$.maxShareNum").value(4));
    }

    @Test
    @DisplayName("구독 서비스 구독하기")
    void givenValidUserIdAndSubscriptionId_whenSubscribe_thenReturnSuccess() throws Exception {
        // Given
        String userId = "user01";
        Long subscriptionId = 1L;
        SubscribeResponse response = new SubscribeResponse(true, "구독이 완료되었습니다.");

        when(subscribeInputBoundary.subscribeSub(userId, subscriptionId)).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/users/{userId}/subscriptions", userId)
                        .param("subscriptionId", subscriptionId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("구독이 완료되었습니다."));
    }

    @Test
    @DisplayName("구독 서비스 취소하기")
    void givenValidUserIdAndSubscriptionId_whenCancelSubscription_thenReturnSuccess() throws Exception {
        // Given
        String userId = "user01";
        Long subscriptionId = 1L;
        SuccessResponse response = new SuccessResponse(true, "구독이 취소되었습니다.");

        when(cancelSubscriptionInputBoundary.cancelSub(userId, subscriptionId)).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/users/{userId}/subscriptions/{subscriptionId}", userId, subscriptionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("구독이 취소되었습니다."));
    }

    @Test
    @DisplayName("구독 카테고리 목록 조회")
    void whenGetSubscriptionCategories_thenReturnCategoryList() throws Exception {
        // Given
        List<CategoryListDTO> categories = Arrays.asList(
                new CategoryListDTO("OTT/동영상"),
                new CategoryListDTO("음악"),
                new CategoryListDTO("식품")
        );

        when(subscriptionCategoriesInputBoundary.getCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/subscriptions/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("OTT/동영상"))
                .andExpect(jsonPath("$[1].categoryName").value("음악"))
                .andExpect(jsonPath("$[2].categoryName").value("식품"));
    }

    @Test
    @DisplayName("카테고리별 구독 서비스 목록 조회")
    void givenValidCategoryId_whenGetSubscriptionsByCategory_thenReturnSubscriptionList() throws Exception {
        // Given
        String categoryId = "OTT";
        List<SubscriptionListResponse> subscriptions = Arrays.asList(
                new SubscriptionListResponse(1L, "넷플릭스", "/images/netflix.png", "글로벌 스트리밍 서비스", 13900),
                new SubscriptionListResponse(2L, "왓챠", "/images/watcha.png", "국내 스트리밍 서비스", 9900)
        );

        when(subscriptionsByCategoryInputBoundary.getSubscriptionsByCategory(categoryId)).thenReturn(subscriptions);

        // When & Then
        mockMvc.perform(get("/api/subscriptions")
                        .param("categoryId", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subscriptionId").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("넷플릭스"))
                .andExpect(jsonPath("$[0].fee").value(13900))
                .andExpect(jsonPath("$[1].subscriptionId").value(2))
                .andExpect(jsonPath("$[1].serviceName").value("왓챠"))
                .andExpect(jsonPath("$[1].fee").value(9900));
    }
}