package com.unicorn.lifesub.mysub.biz.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 내 구독 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MySubscription {
    private String userId;
    private Long subscriptionId;
}
