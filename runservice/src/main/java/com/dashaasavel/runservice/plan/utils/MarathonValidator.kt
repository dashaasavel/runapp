package com.dashaasavel.runservice.plan.utils

private const val MINIMUM_WEEKS = 16

object MarathonValidator {
    fun validateMarathon(countOfWeek: Int) {
        if (countOfWeek <= MINIMUM_WEEKS) {
            throw IllegalArgumentException(CreatingPlanError.MORE_TIME_IS_NEEDED.name)
        }
    }
}