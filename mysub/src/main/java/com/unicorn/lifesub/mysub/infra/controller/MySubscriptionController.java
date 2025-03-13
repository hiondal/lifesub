package com.unicorn.lifesub.mysub.infra.controller;

import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.infra.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 마이구독 서비스 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "마이구독 API", description = "사용자 구독 관리 API")
public class MySubscriptionController {

    private final TotalFeeInputBoundary totalFeeInputBoundary;
    private final MySubscriptionsInputBoundary mySubscriptionsInputBoundary;
    private final SubscriptionDetailInputBoundary subscriptionDetailInputBoundary;
    private final SubscribeInputBoundary subscribeInputBoundary;
    private final CancelSubscriptionInputBoundary cancelSubscriptionInputBoundary;
    private final SubscriptionCategoriesInputBoundary subscriptionCategoriesInputBoundary;
    private final SubscriptionsByCategoryInputBoundary subscriptionsByCategoryInputBoundary;

    /**
     * 사용자의 총 구독료를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 총 구독료 응답
     */
    @GetMapping("/api/users/{userId}/subscriptions/total-fee")
    @Operation(summary = "총 구독료 조회", description = "사용자의 총 구독료를 조회합니다.")
    public ResponseEntity<TotalFeeResponse> getTotalFee(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable String userId) {
        return ResponseEntity.ok(totalFeeInputBoundary.getTotalFee(userId));
    }

    /**
     * 사용자의 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 구독 목록
     */
    @GetMapping("/api/users/{userId}/subscriptions")
    @Operation(summary = "구독 목록 조회", description = "사용자의 구독 서비스 목록을 조회합니다.")
    public ResponseEntity<List<MySubscriptionListResponse>> getMySubscriptions(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable String userId) {
        return ResponseEntity.ok(mySubscriptionsInputBoundary.getMySubscriptions(userId));
    }

    /**
     * 구독 서비스 상세 정보를 조회합니다.
     *
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 서비스 상세 정보
     */
    @GetMapping("/api/subscriptions/{subscriptionId}")
    @Operation(summary = "구독 상세 조회", description = "구독 서비스의 상세 정보를 조회합니다.")
    public ResponseEntity<SubscriptionDetailResponse> getSubscriptionDetail(
            @Parameter(description = "구독 서비스 ID", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionDetailInputBoundary.getSubscriptionDetail(subscriptionId));
    }

    /**
     * 구독 서비스를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 구독 응답
     */
    @PostMapping("/api/users/{userId}/subscriptions")
    @Operation(summary = "구독하기", description = "구독 서비스를 구독합니다.")
    public ResponseEntity<SubscribeResponse> subscribeSub(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable String userId,
            @Parameter(description = "구독 서비스 ID", required = true)
            @RequestParam Long subscriptionId) {
        return ResponseEntity.ok(subscribeInputBoundary.subscribeSub(userId, subscriptionId));
    }

    /**
     * 구독을 취소합니다.
     *
     * @param userId 사용자 ID
     * @param subscriptionId 구독 서비스 ID
     * @return 성공 응답
     */
    @DeleteMapping("/api/users/{userId}/subscriptions/{subscriptionId}")
    @Operation(summary = "구독 취소", description = "구독 서비스를 취소합니다.")
    public ResponseEntity<SuccessResponse> cancelSub(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable String userId,
            @Parameter(description = "구독 서비스 ID", required = true)
            @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(cancelSubscriptionInputBoundary.cancelSub(userId, subscriptionId));
    }

    /**
     * 구독 카테고리 목록을 조회합니다.
     *
     * @return 구독 카테고리 목록
     */
    @GetMapping("/api/subscriptions/categories")
    @Operation(summary = "구독 카테고리 목록 조회", description = "모든 구독 카테고리 목록을 조회합니다.")
    public ResponseEntity<List<CategoryListDTO>> getSubscriptionCategories() {
        return ResponseEntity.ok(subscriptionCategoriesInputBoundary.getCategories());
    }

    /**
     * 카테고리별 구독 서비스 목록을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 구독 서비스 목록
     */
    @GetMapping("/api/subscriptions")
    @Operation(summary = "카테고리별 구독 서비스 목록 조회", description = "카테고리별 구독 서비스 목록을 조회합니다.")
    public ResponseEntity<List<SubscriptionListResponse>> getSubscriptionsByCategory(
            @Parameter(description = "카테고리 ID")
            @RequestParam String categoryId) {
        return ResponseEntity.ok(subscriptionsByCategoryInputBoundary.getSubscriptionsByCategory(categoryId));
    }
}
