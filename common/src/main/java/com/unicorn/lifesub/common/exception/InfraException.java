package com.unicorn.lifesub.common.exception;

import lombok.Getter;

/**
 * 인프라 예외 클래스입니다.
 */
@Getter
public class InfraException extends RuntimeException {

    private final int code;

    /**
     * 메시지를 전달받는 생성자입니다.
     *
     * @param message 예외 메시지
     */
    public InfraException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 메시지와 원인 예외를 전달받는 생성자입니다.
     *
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public InfraException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    /**
     * 코드와 메시지를 전달받는 생성자입니다.
     *
     * @param code 예외 코드
     * @param message 예외 메시지
     */
    public InfraException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 코드, 메시지, 원인 예외를 전달받는 생성자입니다.
     *
     * @param code 예외 코드
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public InfraException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
