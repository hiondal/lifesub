package com.unicorn.lifesub.mysub.biz.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 서비스 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private Long id;
    private String name;
    private String categoryId;
    private String description;
    private int fee;
    private int maxShareNum;
    private String logoUrl;
}
