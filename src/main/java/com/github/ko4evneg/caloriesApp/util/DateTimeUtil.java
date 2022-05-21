package com.github.ko4evneg.caloriesApp.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static LocalDateTime getStartSearchDay(LocalDate date) {
        return (date == null) ? MIN_DATE : date.atStartOfDay();
    }

    public static LocalDateTime getEndSearchDay(LocalDate date) {
        return (date == null) ? MAX_DATE : date.plus(1, ChronoUnit.DAYS).atStartOfDay();
    }

    public static LocalDateTime roundingConvertToEndDateTime(LocalDate date, LocalTime time) {
        return (date == null) ? MAX_DATE :
                (time == null) ? date.plus(1, ChronoUnit.DAYS).atStartOfDay() :
                        LocalDateTime.of(date, time);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static @Nullable
    LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.hasLength(str) ? LocalDate.parse(str) : null;
    }

    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.hasLength(str) ? LocalTime.parse(str) : null;
    }
}
