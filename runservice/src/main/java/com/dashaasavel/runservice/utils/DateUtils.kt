package com.dashaasavel.runservice.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

object DateUtils {
    fun getNextOrSameMonday(): LocalDate {
        return LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
    }

    fun countOfWeeks(date1: LocalDate, date2: LocalDate): Int {
        return ChronoUnit.WEEKS.between(date1, date2).toInt() + 1
    }
}