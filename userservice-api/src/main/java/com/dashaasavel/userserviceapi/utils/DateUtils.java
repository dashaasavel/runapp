package com.dashaasavel.userserviceapi.utils;

import com.dashaasavel.runservice.api.Runservice;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtils {
    public static Runservice.Date createGrpcDate(int year, int month, int day) {
        return Runservice.Date.newBuilder().setYear(year).setMonth(month).setDay(day).build();
    }

    public static Runservice.Date convertToGrpcDate(LocalDate localDate) {
        return Runservice.Date.newBuilder().setYear(localDate.getYear()).setMonth(localDate.getMonthValue()).setDay(localDate.getDayOfMonth()).build();
    }

    public static LocalDate convertToLocalDate(Runservice.Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }

    public static DayOfWeek convertToDayOfWeek(Runservice.DayOfWeek dayOfWeek) {
        return DayOfWeek.valueOf(dayOfWeek.name());
    }

    public static Runservice.DayOfWeek convertToGrpcDayOfWeek(DayOfWeek dayOfWeek) {
        return Runservice.DayOfWeek.valueOf(dayOfWeek.name());
    }
}
