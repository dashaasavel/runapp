package com.dashaasavel.runservice.plan

import com.dashaasavel.runservice.CompetitionRunType
import com.dashaasavel.runservice.Ratio
import com.dashaasavel.runservice.plan.type.MarathonPlanFactory
import com.dashaasavel.runservice.plan.type.PlanAbstractFactory
import com.dashaasavel.runservice.utils.DateUtils
import java.time.LocalDate

class PlanService {
    fun createPlan(userId: Int, type: CompetitionRunType, date: LocalDate, timesAWeek:Int, longRunDistance: Int) {
        val plan = Plan(type, date, timesAWeek, longRunDistance)
        val weeks = DateUtils.countOfWeeks(LocalDate.now(), date)
        val ratio = Ratio.ratio(weeks, longRunDistance)
        val factory: PlanAbstractFactory = when(type) {
            CompetitionRunType.MARATHON -> MarathonPlanFactory()
            else -> throw UnsupportedOperationException()
        }
        val trainings = when(timesAWeek) {
            3 -> factory.createForThreeTimesAWeek(weeks, plan, ratio)
            4 -> factory.createForFourTimesAWeek(weeks, plan, ratio)
            5 -> factory.createForFiveTimesAWeek(weeks, plan, ratio)
            else -> throw UnsupportedOperationException()
        }
    }
}