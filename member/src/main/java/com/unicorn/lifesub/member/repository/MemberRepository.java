package com.unicorn.lifesub.member.repository;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import java.util.Optional;

/**
 * 회원 정보 저장소 인터페이스입니다.
 */
public interface MemberRepository {

    /**
     * 사용자 ID로 회원 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 회원 엔티티 Optional
     */
    Optional<MemberEntity> findByUserId(String userId);

    /**
     * 회원 정보를 저장합니다.
     *
     * @param memberEntity 회원 엔티티
     * @return 저장된 회원 엔티티
     */
    MemberEntity save(MemberEntity memberEntity);
}
