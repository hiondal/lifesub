package com.unicorn.lifesub.member.repository;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 정보 저장소 구현체입니다.
 */
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    /**
     * 사용자 ID로 회원 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 회원 엔티티 Optional
     */
    @Override
    public Optional<MemberEntity> findByUserId(String userId) {
        return memberJpaRepository.findByUserId(userId);
    }

    /**
     * 회원 정보를 저장합니다.
     *
     * @param memberEntity 회원 엔티티
     * @return 저장된 회원 엔티티
     */
    @Override
    public MemberEntity save(MemberEntity memberEntity) {
        return memberJpaRepository.save(memberEntity);
    }
}
