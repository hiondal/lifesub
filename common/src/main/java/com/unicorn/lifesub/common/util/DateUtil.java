package com.unicorn.lifesub.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 날짜 유틸리티 클래스입니다.
 */
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 문자열을 LocalDate로 변환합니다.
     *
     * @param dateString 날짜 문자열 (yyyy-MM-dd 형식)
     * @return LocalDate 객체
     */
    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * LocalDate를 문자열로 변환합니다.
     *
     * @param date LocalDate 객체
     * @return 날짜 문자열 (yyyy-MM-dd 형식)
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * 문자열을 LocalDateTime으로 변환합니다.
     *
     * @param datetimeString 날짜시간 문자열 (yyyy-MM-dd HH:mm:ss 형식)
     * @return LocalDateTime 객체
     */
    public static LocalDateTime parseDateTime(String datetimeString) {
        return LocalDateTime.parse(datetimeString, DATETIME_FORMATTER);
    }

    /**
     * LocalDateTime을 문자열로 변환합니다.
     *
     * @param datetime LocalDateTime 객체
     * @return 날짜시간 문자열 (yyyy-MM-dd HH:mm:ss 형식)
     */
    public static String formatDateTime(LocalDateTime datetime) {
        return datetime.format(DATETIME_FORMATTER);
    }
}
