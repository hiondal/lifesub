package com.unicorn.lifesub.mysub.biz.usecase.in;

import com.unicorn.lifesub.mysub.infra.dto.CategoryListDTO;

import java.util.List;

/**
 * a구독 카테고리 목록 조회 유스케이스 인터페이스입니다.
 */
public interface SubscriptionCategoriesInputBoundary {
    
    /**
     * 구독 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    List<CategoryListDTO> getCategories();
}
