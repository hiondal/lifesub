package com.unicorn.lifesub.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 회원 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String userId;
    private String userName;
    private String password;
    private String bankName;
    private String bankAccount;
    private Integer characterId;
}
