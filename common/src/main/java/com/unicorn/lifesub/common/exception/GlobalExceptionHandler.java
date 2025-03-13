package com.unicorn.lifesub.common.exception;

import com.unicorn.lifesub.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 전역 예외 처리 클래스입니다.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외를 처리합니다.
     *
     * @param e 비즈니스 예외
     * @return 응답 엔티티
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException e) {
        log.error("Business Exception: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 인프라 예외를 처리합니다.
     *
     * @param e 인프라 예외
     * @return 응답 엔티티
     */
    @ExceptionHandler(InfraException.class)
    public ResponseEntity<ErrorResponse> handleInfraException(InfraException e) {
        log.error("Infrastructure Exception: {}", e.getMessage(), e);
        HttpStatus status = HttpStatus.valueOf(e.getCode());
        ErrorResponse response = new ErrorResponse(status.value(), e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    /**
     * 유효성 검사 예외를 처리합니다.
     *
     * @param e 유효성 검사 예외
     * @return 응답 엔티티
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Validation Exception: {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        
        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
                
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 모든 예외를 처리합니다.
     *
     * @param e 예외
     * @return 응답 엔티티
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
