package com.dashaasavel.grpcmessages.utils;

import com.dashaasavel.date.api.Date;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {

    public static Date convertToGrpcDate(LocalDate localDate) {
        return Date.newBuilder().setYear(localDate.getYear()).setMonth(localDate.getMonthValue()).setDay(localDate.getDayOfMonth()).build();
    }

    public static DayOfWeek convertToDayOfWeek(com.dashaasavel.date.api.DayOfWeek dayOfWeek) {
        return DayOfWeek.valueOf(dayOfWeek.name());
    }

    public static com.dashaasavel.date.api.DayOfWeek convertToGrpcDayOfWeek(DayOfWeek dayOfWeek) {
        return com.dashaasavel.date.api.DayOfWeek.valueOf(dayOfWeek.name());
    }
}
