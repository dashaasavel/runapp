package com.dashaasavel.runservice.plan.utils

import com.dashaasavel.runapplib.grpc.error.CreatingPlanError
import com.dashaasavel.runservice.plan.CreatingPlanException

private const val MINIMUM_WEEKS = 16

object MarathonValidator {
    fun validateMarathon(countOfWeek: Int) {
        if (countOfWeek <= MINIMUM_WEEKS) {
            throw CreatingPlanException(CreatingPlanError.MORE_TIME_IS_NEEDED)
        }
    }
}