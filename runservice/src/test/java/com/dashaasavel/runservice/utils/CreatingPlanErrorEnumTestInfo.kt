package com.dashaasavel.runservice.utils

import com.dashaasavel.runservice.plan.utils.CreatingPlanError
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test

class CreatingPlanErrorEnumTestInfo {
    @Test
    fun `local and api enum`() {
        val localValues = CreatingPlanError.values().map { it.name }
        val apiValues = com.dashaasavel.runservice.api.Runservice.CreatingPlanError.values().map { it.name }
        assertIterableEquals(localValues, apiValues)
    }
}