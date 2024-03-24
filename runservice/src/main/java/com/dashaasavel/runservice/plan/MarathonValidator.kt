package com.dashaasavel.runservice.plan

object MarathonValidator {
    fun validateMarathon(countOfWeek: Int) {
        if (countOfWeek <= 16) {
            throw IllegalArgumentException("Count of weeks for a marathon preparing should be more than 16")
        }
    }
}