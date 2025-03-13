// File: lifesub/member/src/main/java/com/unicorn/lifesub/member/config/DataLoader.java
package com.unicorn.lifesub.member.config;

import com.unicorn.lifesub.member.repository.entity.MemberEntity;
import com.unicorn.lifesub.member.repository.jpa.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * 애플리케이션 시작 시 초기 데이터를 로드하는 클래스입니다.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberJpaRepository memberJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 기존 사용자 데이터가 없을 경우에만 초기 데이터 생성
        if (memberJpaRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode("Passw0rd");
            LocalDateTime now = LocalDateTime.now();

            // 은행 정보 목록
            List<String> bankNames = Arrays.asList("신한은행", "국민은행", "우리은행", "하나은행", "농협은행");
            Random random = new Random();

            IntStream.rangeClosed(1, 10).forEach(i -> {
                String userId = String.format("user%02d", i);
                String bankName = bankNames.get(random.nextInt(bankNames.size()));
                String bankAccount = String.format("%03d-%02d-%08d",
                        100 + random.nextInt(900),
                        10 + random.nextInt(90),
                        10000000 + random.nextInt(90000000));
                Integer characterId = 1 + random.nextInt(5); // 1~5 사이의 캐릭터 ID

                MemberEntity member = MemberEntity.builder()
                        .userId(userId)
                        .userName("사용자" + i)
                        .password(encodedPassword)
                        .bankName(bankName)
                        .bankAccount(bankAccount)
                        .characterId(characterId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

                memberJpaRepository.save(member);
            });

            System.out.println("초기 사용자 데이터 생성 완료: 10개의 계정이 생성되었습니다.");
        }
    }
}