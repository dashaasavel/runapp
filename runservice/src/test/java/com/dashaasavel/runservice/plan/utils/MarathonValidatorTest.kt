package com.dashaasavel.runservice.plan.utils

import com.dashaasavel.runservice.plan.CreatingPlanException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MarathonValidatorTest {
    @Test
    fun `when count of weeks is correct then success`() {
        assertDoesNotThrow {MarathonValidator.validateMarathon(MINIMUM_WEEKS) }
    }

    @Test
    fun `when count of weeks is incorrect then throws`() {
        assertThrows(CreatingPlanException::class.java) { MarathonValidator.validateMarathon(MINIMUM_WEEKS - 1) }
    }
}