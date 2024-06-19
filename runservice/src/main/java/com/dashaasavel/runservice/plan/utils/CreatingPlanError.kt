package com.dashaasavel.runservice.plan.utils

enum class CreatingPlanError(
    private val message: String
) {
    MORE_TIME_IS_NEEDED("Need at least %d weeks to prepare for a %s"),
    COMPETITION_TYPE_NOT_SUPPORTED_NOW("Competition type %s is not supported now"),
    NOT_SUPPORTED_COUNT_TIMES_A_WEEK("Specify from 3 to 5 times a week");

}