package com.dashaasavel.runservice.plan.utils

import com.dashaasavel.runservice.plan.training.CompetitionRunType

private const val MINIMUM_WEEKS = 16

object MarathonValidator {
    fun validateMarathon(countOfWeek: Int) {
        if (countOfWeek <= MINIMUM_WEEKS) {
            throw IllegalArgumentException(CreatingPlanError.MORE_TIME_IS_NEEDED.name)
        }
    }
}