package com.dashaasavel.runservice.plan.type

import com.dashaasavel.runservice.training.Training
import com.dashaasavel.runservice.plan.PlanInfo
import com.dashaasavel.userserviceapi.utils.CompetitionRunType
import java.lang.UnsupportedOperationException
import java.time.DayOfWeek
import java.time.DayOfWeek.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.math.ceil

abstract class PlanAbstractFactory {
    abstract fun getCompetitionRunType(): CompetitionRunType
    abstract fun validate(planInfo: PlanInfo)
    abstract fun createForThreeTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training>
    abstract fun createForFourTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training>
    abstract fun createForFiveTimesAWeek(weeks: Int, planInfo: PlanInfo, ratio: IntArray): List<Training>

    fun datesForAWeek(startWeek: LocalDate, daysOfWeek: List<DayOfWeek>): Array<LocalDate> {
        val dates = Array<LocalDate>(daysOfWeek.size) { LocalDate.now() }
        for (i in daysOfWeek.indices) {
            dates[i] = startWeek.with(TemporalAdjusters.nextOrSame(daysOfWeek[i]))
        }
        return dates
    }

    fun distanceForTrainings(weekDistance: Int): IntArray {
        val dist = IntArray(3)
        dist[2] = ceil(weekDistance.toDouble() / 2).toInt()
        dist[1] = weekDistance / 4
        dist[0] = weekDistance - dist[1] - dist[2]
        return dist
    }

    fun createLastWeek(
        trainingNumber: Int,
        weekNumber: Int,
        startWeek: LocalDate,
        competitionDate: LocalDate
    ): List<Training> {
        val lastWeek = ArrayList<Training>(3)
        // сомнительно, но окэй
        return if (competitionDate.dayOfWeek == SUNDAY) {
            val dates = datesForAWeek(startWeek, listOf(MONDAY, WEDNESDAY, FRIDAY, SUNDAY, competitionDate.dayOfWeek))
            lastWeek += Training.regularRunning(10, trainingNumber + 1, weekNumber, dates[0])
            lastWeek += Training.regularRunning(7, trainingNumber + 2, weekNumber, dates[1])
            lastWeek += Training.regularRunning(5, trainingNumber + 3, weekNumber, dates[2])
            lastWeek += Training.regularRunning(4, trainingNumber + 4, weekNumber, dates[3])
            lastWeek += Training.finalRunning(trainingNumber + 5, weekNumber, dates[4], getCompetitionRunType())
            lastWeek
        } else if (competitionDate.dayOfWeek == SATURDAY) {
            val dates = datesForAWeek(startWeek, listOf(MONDAY, WEDNESDAY, FRIDAY, competitionDate.dayOfWeek))
            lastWeek += Training.regularRunning(10, trainingNumber + 1, weekNumber, dates[0])
            lastWeek += Training.regularRunning(7, trainingNumber + 2, weekNumber, dates[1])
            lastWeek += Training.regularRunning(4, trainingNumber + 3, weekNumber, dates[2])
            lastWeek += Training.finalRunning(trainingNumber + 4, weekNumber, dates[3], getCompetitionRunType())
            lastWeek
        } else throw UnsupportedOperationException("Can't create last week not for a weekend competition")
    }

    /**
     * За день до марафона 3-5 км
     * До этого через день 5-7 км, лучше включить еще скоростные
     * То есть идем от даты марафона (рассчитываем что он будет во 2ой половине недели (выходные), -> ПН + СР + ПТ/ПТ+СБ
     * туду -- сделать для первой половины, учитывая предпоследнюю тогда)
     */
}