package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.CompetitionRunType
import java.time.LocalDate

class Plan(
    val competitionRunType: CompetitionRunType,
    val date: LocalDate,
    val timesAWeek:Int,
    val longRunDistance: Int
) {
}