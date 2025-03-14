// File: lifesub/recommend/src/main/java/com/unicorn/lifesub/recommend/config/DataLoader.java
package com.unicorn.lifesub.recommend.config;

import com.unicorn.lifesub.recommend.repository.entity.CategoryEntity;
import com.unicorn.lifesub.recommend.repository.entity.SpendingEntity;
import com.unicorn.lifesub.recommend.repository.jpa.CategoryJpaRepository;
import com.unicorn.lifesub.recommend.repository.jpa.SpendingJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner  {

    private final CategoryJpaRepository categoryRepository;
    private final SpendingJpaRepository spendingRepository;
    private final Random random = new Random();

    private static final List<String> SPENDING_CATEGORIES = Arrays.asList(
            "COSMETICS", "ENTERTAINMENT", "EDUCATION", "RESTAURANT", "MUSIC", "DAILY"
    );

    private static final List<String> SUBSCRIPTION_CATEGORIES = Arrays.asList(
            "BEAUTY", "OTT", "EDU", "FOOD", "MUSIC", "LIFE"
    );

    @Override                   // CommandLineRunner의 run 메소드 구현
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        initSpendingHistory();
        initRecommendedCategories();
        log.info("Sample data initialization completed.");
    }

    private void initSpendingHistory() {
        // 기존 데이터 삭제
        spendingRepository.deleteAll();

        List<SpendingEntity> spendings = new ArrayList<>();
        LocalDate now = LocalDate.now();

        // user01 ~ user10까지의 지출 데이터 생성
        for (int i = 1; i <= 10; i++) {
            String userId = String.format("user%02d", i);

            // 각 사용자별로 지난 한달간 5~10건의 지출 데이터 생성
            int numTransactions = 5 + random.nextInt(6);

            for (int j = 0; j < numTransactions; j++) {
                String category = SPENDING_CATEGORIES.get(random.nextInt(SPENDING_CATEGORIES.size()));
                BigDecimal amount = new BigDecimal((50 + random.nextInt(451)) * 1000L); // 5만원 ~ 50만원 사이
                int daysAgo = random.nextInt(30); // 최근 30일 이내

                spendings.add(SpendingEntity.builder()
                        .userId(userId)
                        .category(category)
                        .amount(amount)
                        .spendDate(now.minusDays(daysAgo))
                        .createdAt(LocalDateTime.now())
                        .build());
            }
        }

        spendingRepository.saveAll(spendings);
        log.info("Spending history data initialized with {} records", spendings.size());
    }

    private void initRecommendedCategories() {
        // 기존 데이터 삭제
        categoryRepository.deleteAll();

        // 지출 카테고리와 추천 구독 카테고리 매핑 데이터 생성
        List<CategoryEntity> categories = Arrays.asList(
                CategoryEntity.builder()
                        .id("COSMETICS_BEAUTY")
                        .categoryName("뷰티")
                        .spendingCategory("COSMETICS")
                        .build(),
                CategoryEntity.builder()
                        .id("ENTERTAINMENT_OTT")
                        .categoryName("OTT/동영상")
                        .spendingCategory("ENTERTAINMENT")
                        .build(),
                CategoryEntity.builder()
                        .id("EDUCATION_EDU")
                        .categoryName("교육")
                        .spendingCategory("EDUCATION")
                        .build(),
                CategoryEntity.builder()
                        .id("RESTAURANT_FOOD")
                        .categoryName("식품")
                        .spendingCategory("RESTAURANT")
                        .build(),
                CategoryEntity.builder()
                        .id("MUSIC_MUSIC")
                        .categoryName("음악")
                        .spendingCategory("MUSIC")
                        .build(),
                CategoryEntity.builder()
                        .id("DAILY_LIFE")
                        .categoryName("생활")
                        .spendingCategory("DAILY")
                        .build()
        );

        categoryRepository.saveAll(categories);
        log.info("Recommended categories data initialized with {} records", categories.size());
    }
}