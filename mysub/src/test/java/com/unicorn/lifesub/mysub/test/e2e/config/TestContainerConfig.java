package com.unicorn.lifesub.mysub.test.e2e.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 테스트 컨테이너 설정 클래스입니다.
 */
@Testcontainers
public abstract class TestContainerConfig {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13.2-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    /**
     * PostgreSQL 컨테이너의 속성을 Spring 환경에 등록합니다.
     *
     * @param registry 동적 속성 레지스트리
     */
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }
}