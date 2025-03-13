package com.unicorn.lifesub.recommend.repository.entity;

import com.unicorn.lifesub.recommend.domain.Spending;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 지출 정보 엔티티 클래스입니다.
 */
@Entity
@Table(name = "spending_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "spend_date")
    private LocalDate spendDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 지출 정보 도메인 객체
     */
    public Spending toDomain() {
        return new Spending(id, userId, category, amount);
    }

    /**
     * 도메인 객체로부터 엔티티를 생성합니다.
     *
     * @param spending 지출 정보 도메인 객체
     * @return 지출 정보 엔티티
     */
    public static SpendingEntity fromDomain(Spending spending) {
        return SpendingEntity.builder()
                .id(spending.getId())
                .userId(spending.getUserId())
                .category(spending.getCategory())
                .amount(spending.getAmount())
                .spendDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
