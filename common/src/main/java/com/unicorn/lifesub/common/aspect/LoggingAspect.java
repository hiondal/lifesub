package com.unicorn.lifesub.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 로깅 관점 클래스입니다.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * 메서드 시작 시 로그를 기록합니다.
     *
     * @param joinPoint 결합점
     */
    @Before("execution(* com.unicorn.lifesub..*Controller.*(..)) || execution(* com.unicorn.lifesub..*Service.*(..))")
    public void logMethodStart(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("Starting {} in {}", methodName, className);
    }

    /**
     * 메서드 종료 시 로그를 기록합니다.
     *
     * @param joinPoint 결합점
     * @param result 결과 객체
     */
    @AfterReturning(pointcut = "execution(* com.unicorn.lifesub..*Controller.*(..)) || execution(* com.unicorn.lifesub..*Service.*(..))", returning = "result")
    public void logMethodEnd(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.debug("Completed {} in {} with result: {}", methodName, className, result);
    }

    /**
     * 메서드 예외 발생 시 로그를 기록합니다.
     *
     * @param joinPoint 결합점
     * @param e 예외
     */
    @AfterThrowing(pointcut = "execution(* com.unicorn.lifesub..*Controller.*(..)) || execution(* com.unicorn.lifesub..*Service.*(..))", throwing = "e")
    public void logMethodException(JoinPoint joinPoint, Exception e) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception in {} {} - {}", className, methodName, e.getMessage(), e);
    }
}
