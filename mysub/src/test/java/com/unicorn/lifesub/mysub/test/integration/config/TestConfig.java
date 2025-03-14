package com.unicorn.lifesub.mysub.test.integration.config;

import com.unicorn.lifesub.mysub.biz.usecase.in.*;
import com.unicorn.lifesub.mysub.infra.controller.MySubscriptionController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.Mockito.mock;

/**
 * 통합 테스트를 위한 빈 설정 클래스입니다.
 */
@Configuration
public class TestConfig implements WebMvcConfigurer {

    @Bean
    public TotalFeeInputBoundary totalFeeInputBoundary() {
        return mock(TotalFeeInputBoundary.class);
    }

    @Bean
    public MySubscriptionsInputBoundary mySubscriptionsInputBoundary() {
        return mock(MySubscriptionsInputBoundary.class);
    }

    @Bean
    public SubscriptionDetailInputBoundary subscriptionDetailInputBoundary() {
        return mock(SubscriptionDetailInputBoundary.class);
    }

    @Bean
    public SubscribeInputBoundary subscribeInputBoundary() {
        return mock(SubscribeInputBoundary.class);
    }

    @Bean
    public CancelSubscriptionInputBoundary cancelSubscriptionInputBoundary() {
        return mock(CancelSubscriptionInputBoundary.class);
    }

    @Bean
    public SubscriptionCategoriesInputBoundary subscriptionCategoriesInputBoundary() {
        return mock(SubscriptionCategoriesInputBoundary.class);
    }

    @Bean
    public SubscriptionsByCategoryInputBoundary subscriptionsByCategoryInputBoundary() {
        return mock(SubscriptionsByCategoryInputBoundary.class);
    }

    @Bean
    public MySubscriptionController mySubscriptionController() {
        return new MySubscriptionController(
                totalFeeInputBoundary(),
                mySubscriptionsInputBoundary(),
                subscriptionDetailInputBoundary(),
                subscribeInputBoundary(),
                cancelSubscriptionInputBoundary(),
                subscriptionCategoriesInputBoundary(),
                subscriptionsByCategoryInputBoundary()
        );
    }
}