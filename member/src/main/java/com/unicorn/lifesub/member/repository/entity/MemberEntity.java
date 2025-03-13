package com.unicorn.lifesub.member.repository.entity;

import com.unicorn.lifesub.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 정보 엔티티 클래스입니다.
 */
@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "character_id")
    private Integer characterId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 회원 도메인 객체
     */
    public Member toDomain() {
        return Member.builder()
                .userId(userId)
                .userName(userName)
                .password(password)
                .bankName(bankName)
                .bankAccount(bankAccount)
                .characterId(characterId)
                .build();
    }

    /**
     * 도메인 객체로부터 엔티티를 생성합니다.
     *
     * @param member 회원 도메인 객체
     * @return 회원 엔티티
     */
    public static MemberEntity fromDomain(Member member) {
        return MemberEntity.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .password(member.getPassword())
                .bankName(member.getBankName())
                .bankAccount(member.getBankAccount())
                .characterId(member.getCharacterId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
