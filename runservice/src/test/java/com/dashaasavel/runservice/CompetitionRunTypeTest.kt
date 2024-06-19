package com.dashaasavel.runservice

import com.dashaasavel.runservice.plan.training.CompetitionRunType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CompetitionRunTypeTest {
    @Test
    fun `competition run type local and api are same`() {
        val localValues = CompetitionRunType.values().map { it.name }
        val apiValues = com.dashaasavel.runservice.api.Runservice.CompetitionRunType.values().map { it.name }
        assertIterableEquals(localValues, apiValues)
    }
}