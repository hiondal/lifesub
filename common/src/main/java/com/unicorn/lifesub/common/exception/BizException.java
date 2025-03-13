package com.unicorn.lifesub.common.exception;

/**
 * 비즈니스 예외 클래스입니다.
 */
public class BizException extends RuntimeException {

    /**
     * 메시지를 전달받는 생성자입니다.
     *
     * @param message 예외 메시지
     */
    public BizException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외를 전달받는 생성자입니다.
     *
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
