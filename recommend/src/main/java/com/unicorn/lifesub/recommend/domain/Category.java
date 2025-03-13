package com.unicorn.lifesub.recommend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String categoryName;
    private String spendingCategory;
}
