package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.training.Training
import com.dashaasavel.grpcmessages.utils.CompetitionRunType
import java.time.DayOfWeek
import java.time.LocalDate

class PlanInfo(
    val id: Int? = null,
    var trainingsId: String? = null,
    val userId: Int,
    val competitionRunType: CompetitionRunType,
    val competitionDate: LocalDate,
    val daysOfWeek: List<DayOfWeek>,
    val longRunDistance: Int
)

class Plan(
    val info: PlanInfo,
    var trainings: List<Training>? = null
)