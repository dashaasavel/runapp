package com.dashaasavel.runservice.utils

import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateUtilsTest {
    @Test
    fun countOfWeeks() {
        val first = LocalDate.of(2024, 3, 24)
        val second = LocalDate.of(2024, 3, 25)
        val weeks = DateUtils.countOfWeeks(first, second)
        println(weeks)
    }
}