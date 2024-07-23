package com.dashaasavel.userserviceapi.utils;

import com.dashaasavel.date.api.Date;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {
    public static Date createGrpcDate(int year, int month, int day) {
        return Date.newBuilder().setYear(year).setMonth(month).setDay(day).build();
    }

    public static Date convertToGrpcDate(LocalDate localDate) {
        return Date.newBuilder().setYear(localDate.getYear()).setMonth(localDate.getMonthValue()).setDay(localDate.getDayOfMonth()).build();
    }

    public static LocalDate convertToLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static DayOfWeek convertToDayOfWeek(com.dashaasavel.date.api.DayOfWeek dayOfWeek) {
        return DayOfWeek.valueOf(dayOfWeek.name());
    }

    public static com.dashaasavel.date.api.DayOfWeek convertToGrpcDayOfWeek(DayOfWeek dayOfWeek) {
        return com.dashaasavel.date.api.DayOfWeek.valueOf(dayOfWeek.name());
    }
}
