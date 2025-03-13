package com.unicorn.lifesub.recommend.service;

import com.unicorn.lifesub.common.exception.BizException;
import com.unicorn.lifesub.recommend.domain.Category;
import com.unicorn.lifesub.recommend.dto.RecommendCategoryDTO;
import com.unicorn.lifesub.recommend.repository.CategoryRepository;
import com.unicorn.lifesub.recommend.repository.SpendingRepository;
import com.unicorn.lifesub.recommend.repository.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 구독추천 서비스 구현체입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendServiceImpl implements RecommendService {

    private final SpendingRepository spendingRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 최고 지출 카테고리 기반 구독 추천을 제공합니다.
     *
     * @param userId 사용자 ID
     * @return 추천 카테고리 응답
     */
    @Override
    @Transactional(readOnly = true)
    public RecommendCategoryDTO getRecommendCategories(String userId) {
        log.debug("사용자 {} 추천 구독 카테고리 조회", userId);
        
        // 최고 지출 카테고리 조회
        String topSpendingCategory = spendingRepository.findTopCategoryByUserId(userId);
        if (topSpendingCategory == null) {
            log.warn("사용자 {} 지출 데이터가 없습니다.", userId);
            return new RecommendCategoryDTO(
                    "기본 추천 카테고리",
                    "/images/default_category.png",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            );
        }
        
        // 매핑된 구독 카테고리 조회
        CategoryEntity categoryEntity = categoryRepository.findBySpendingCategory(topSpendingCategory)
                .orElseThrow(() -> new BizException("매핑된 구독 카테고리가 없습니다."));
                
        Category category = categoryEntity.toDomain();
        
        // 응답 생성
        return new RecommendCategoryDTO(
                category.getCategoryName(),
                "/images/" + topSpendingCategory.toLowerCase() + ".png",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }
}
