package com.unicorn.lifesub.mysub.infra.gateway.entity;

import com.unicorn.lifesub.mysub.biz.domain.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class CategoryEntity {
    @Id
    private String categoryId;
    private String categoryName;

    @Builder
    public CategoryEntity(String categoryId, String name) {
        this.categoryId = categoryId;
        this.categoryName = name;
    }

    public Category toDomain() {
        return Category.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .build();
    }
}
