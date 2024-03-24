package com.dashaasavel.runservice.plan.type

import com.dashaasavel.runservice.Training
import com.dashaasavel.runservice.plan.Plan
import java.time.DayOfWeek.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.math.ceil

abstract class PlanAbstractFactory {
    private val threeDaysAWeek = arrayOf(TUESDAY, THURSDAY, SUNDAY)
    private val fourDaysAWeek = arrayOf(TUESDAY, WEDNESDAY, FRIDAY, SUNDAY)
    private val fiveDaysAWeek = arrayOf(TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SUNDAY)

    abstract fun validate(plan: Plan)
    abstract fun createForThreeTimesAWeek(weeks: Int, plan: Plan, ratio: IntArray): List<Training>
    abstract fun createForFourTimesAWeek(weeks: Int, plan: Plan, ratio: IntArray): List<Training>
    abstract fun createForFiveTimesAWeek(weeks: Int, plan: Plan, ratio: IntArray): List<Training>

    fun datesForAWeek(startWeek: LocalDate, timesAWeek: Int): Array<LocalDate> {
        val dates = Array<LocalDate>(timesAWeek) { LocalDate.now()}
        val current = when (timesAWeek) {
            3 -> threeDaysAWeek
            4 -> fourDaysAWeek
            else -> fiveDaysAWeek
        }
        for (i in 0 until timesAWeek) {
            dates[i] = startWeek.with(TemporalAdjusters.next(current[i]))
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

    fun createLastWeek(trainingNumber: Int, weekNumber: Int, startWeek: LocalDate): List<Training> {
        val lastWeek = mutableListOf<Training>()
        val dates = datesForAWeek(startWeek, 3)
        lastWeek += Training.regularRunning(5, trainingNumber + 1, weekNumber, dates[0])
        lastWeek += Training.regularRunning(5, trainingNumber + 2, weekNumber, dates[1])
        lastWeek += Training.finalRunning(trainingNumber + 3, weekNumber, dates[2])
        return lastWeek
    }
}