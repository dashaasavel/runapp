package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.plan.training.CompetitionRunType
import com.dashaasavel.runservice.Training
import java.time.DayOfWeek
import java.time.LocalDate

class PlanInfo(
    val id: Int? = null,
    var trainingListId: String? = null,
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