package com.unicorn.lifesub.recommend.repository.entity;

import com.unicorn.lifesub.recommend.domain.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카테고리 엔티티 클래스입니다.
 */
@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {

    @Id
    private String id;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "spending_category", nullable = false)
    private String spendingCategory;

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 카테고리 도메인 객체
     */
    public Category toDomain() {
        return new Category(id, categoryName, spendingCategory);
    }

    /**
     * 도메인 객체로부터 엔티티를 생성합니다.
     *
     * @param category 카테고리 도메인 객체
     * @return 카테고리 엔티티
     */
    public static CategoryEntity fromDomain(Category category) {
        return CategoryEntity.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .spendingCategory(category.getSpendingCategory())
                .build();
    }
}
