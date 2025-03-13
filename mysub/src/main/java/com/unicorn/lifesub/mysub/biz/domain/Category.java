package com.unicorn.lifesub.mysub.biz.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

/**
 * 카테고리 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private String categoryId;
    private String categoryName;
}
