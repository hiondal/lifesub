package com.unicorn.lifesub.member.repository.jpa;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 정보 JPA 저장소 인터페이스입니다.
 */
@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, String> {

    /**
     * 사용자 ID로 회원 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 회원 엔티티 Optional
     */
    Optional<MemberEntity> findByUserId(String userId);
}
