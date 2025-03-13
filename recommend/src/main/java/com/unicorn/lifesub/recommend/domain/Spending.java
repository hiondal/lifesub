package com.unicorn.lifesub.recommend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 지출 정보 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Spending {
    private Long id;
    private String userId;
    private String category;
    private BigDecimal amount;
}
